/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.operators.subgraph_mining;

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.PropertyGraphDB;
import org.biiig.dmgm.api.SpecializableCachedGraph;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph.SpecializableAdjacencyList;
import org.biiig.dmgm.impl.operators.CollectionOperatorBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.GrowAllChildren;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.InitializeParents;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.IsMinimal;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.FrequentSpecializations;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class GeneralizedSubgraphsBase<S> extends CollectionOperatorBase {
  private static final String LEVEL_SEPARATOR = "_";
  private static final String FREQUENT_SUBGRAPH = "Frequent Subgraph";
  private static final String FREQUENT_SUBGRAPHS = "Frequent Subgraphs";
  protected final PropertyGraphDB database;
  protected final int maxEdgeCount;
  protected final int patternLabel;
  protected final int collectionLabel;

  protected final float minSupportRel;


  public GeneralizedSubgraphsBase(int maxEdgeCount, PropertyGraphDB database, float minSupportRel) {
    this.minSupportRel = minSupportRel;
    this.maxEdgeCount = maxEdgeCount;
    this.database = database;
    patternLabel = database.encode(FREQUENT_SUBGRAPH);
    collectionLabel = database.encode(FREQUENT_SUBGRAPHS);
  }

  @Override
  public Long apply(Long inputCollectionId) {
    List<CachedGraph> input = database.getCachedCollection(inputCollectionId);

    Map<Integer, Pair<Integer, int[]>> taxonomyPaths = generalize(input);

    taxonomyPaths
      .entrySet()
      .forEach(e -> {

        List<String> path =Lists.newArrayList(database.decode(e.getValue().getKey()));

        IntStream.of(e.getValue().getValue()).mapToObj(database::decode)
          .forEach(s -> path.add(s));
      });

    Map<Long, SpecializableCachedGraph> indexedGraphs = (parallel ? input.parallelStream() : input.stream())
      .collect(Collectors.toMap(
        CachedGraph::getId,
        g -> {
          int vertexCount = g.getVertexCount();
          int[] vertexLabels = new int[vertexCount];
          int[][] taxonomyTails = new int[vertexCount][];

          for (int i = 0; i < vertexCount; i++) {
            int bottomLevel = g.getVertexLabel(i);
            Pair<Integer, int[]> taxonomyPath = taxonomyPaths.get(bottomLevel);

            if (taxonomyPath == null) {
              vertexLabels[i] = bottomLevel;
            } else {
              vertexLabels[i] = taxonomyPath.getKey();
              taxonomyTails[i] = taxonomyPath.getValue();
            }
          }

          return new SpecializableAdjacencyList(
            g.getId(),
            g.getLabel(),
            vertexLabels,
            g.getEdgeLabels(),
            g.getSourceIds(),
            g.getTargetIds(),
            taxonomyTails);
        }
      ));

    SupportMethods<S> supportMethods = getAggregateAndFilter(indexedGraphs);

    Stream<Pair<DFSCode,DFSEmbedding>> reports = getSingleEdgeReports(indexedGraphs);

    Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> filtered =
      supportMethods.aggregateAndFilter(reports);

    List<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> parents = specialize(filtered, supportMethods, indexedGraphs);

    long[] graphIds = supportMethods.output(parents);

    int edgeCount = 1;
    while (edgeCount < maxEdgeCount && !parents.isEmpty()) {
      reports = (parallel ? parents.parallelStream() : parents.stream())
        .map(Pair::getKey)
        .flatMap(new GrowAllChildren(indexedGraphs));

      filtered = supportMethods.aggregateAndFilter(reports)
        .filter(e -> new IsMinimal().test(e.getKey().getKey()));

      parents = specialize(filtered, supportMethods, indexedGraphs);

      graphIds = ArrayUtils.addAll(graphIds, supportMethods.output(parents));

      edgeCount++;
    }

    return database.createCollection(collectionLabel, graphIds);
  }

  public abstract SupportMethods<S> getAggregateAndFilter(Map<Long, SpecializableCachedGraph> input);

  private Map<Integer, Pair<Integer, int[]>> generalize(List<CachedGraph> input) {
    Map<Integer, Long> vertexLabelSupport = getVertexLabelSupport(input);

    return vertexLabelSupport
      .keySet()
      .stream()
      .map(database::decode)
      .filter(s -> s.contains(LEVEL_SEPARATOR))
      .map(s -> {
        int[] ints = new int[] {database.encode(s)};

        while (s.contains(LEVEL_SEPARATOR)) {
          s = StringUtils.substringBeforeLast(s, LEVEL_SEPARATOR);
          ints = ArrayUtils.add(ints, database.encode(s));
        }

        ArrayUtils.reverse(ints);

        return ints;
      })
      .collect(Collectors.toMap(
        a -> a[a.length - 1],
        a -> new Pair<>(a[0], ArrayUtils.subarray(a, 1, a.length))));
  }

  private List<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> specialize(
    Stream<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> filtered, SupportMethods<S> supportMethods, Map<Long, SpecializableCachedGraph> indexedGraphs) {

    return filtered
      .flatMap(new FrequentSpecializations<>(supportMethods, indexedGraphs))
      .collect(Collectors.toList());
  }



  public Stream<Pair<DFSCode,DFSEmbedding>> getSingleEdgeReports(Map<Long, SpecializableCachedGraph> input) {
    Collection<SpecializableCachedGraph> values = input.values();
    return (parallel ? values.parallelStream() : values.stream())
      .flatMap(new InitializeParents(patternLabel));
  }

  public Map<Integer, Long> getVertexLabelSupport(List<CachedGraph> input) {
    return (parallel ? input.parallelStream() : input.stream())
      .flatMapToInt(g -> IntStream
        .of(g.getVertexLabels())
        .distinct())
      .boxed()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .map(e -> new Pair<>(database.decode(e.getKey()), e.getValue()))
      .flatMap(p -> {
        String child = p.getKey();

        int[] labels = new int[] {database.encode(child)};

        while (child.contains(LEVEL_SEPARATOR)) {
          String parent = StringUtils.substringBeforeLast(child, LEVEL_SEPARATOR);
          labels = ArrayUtils.add(labels, database.encode(parent));
          child = parent;
        }

        return IntStream.of(labels)
          .mapToObj(parent -> new Pair<>(parent, p.getValue()));
      })
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet()
      .stream()
      .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToLong(i -> i).sum()));
  }
}
