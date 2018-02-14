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
import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportSpecialization;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.FrequentSpecializations;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.SpecializableAdjacencyList;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.SpecializableCachedGraph;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class GeneralizedSubgraphsBase<S> extends SubgraphMiningBase<SpecializableCachedGraph, S> {
  public GeneralizedSubgraphsBase(PropertyGraphDB db, boolean parallel, float minSupportRel, int maxEdgeCount) {
    super(db, parallel, minSupportRel, maxEdgeCount);
  }

  @Override
  public Map<Long, SpecializableCachedGraph> preProcess(Long inputCollectionId) {
    List<CachedGraph> input = db.getCachedCollection(inputCollectionId);

    Map<Integer, Pair<Integer, int[]>> taxonomyPaths = getLabelTaxonomyPaths(getVertexLabelSupport(input));

    taxonomyPaths
      .forEach((key, value) -> {
        List<String> path = Lists.newArrayList(db.decode(value.getKey()));

        IntStream.of(value.getValue()).mapToObj(db::decode)
          .forEach(path::add);
      });

    return getParallelizableStream(input)
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
  }

  @Override
  public List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> postProcess(Map<Long, SpecializableCachedGraph> indexedGraphs, List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> filtered, SupportSpecialization afo) {
    return getParallelizableStream(filtered)
      .flatMap(new FrequentSpecializations<>(afo, indexedGraphs))
      .collect(Collectors.toList());
  }

  @Override
  public Map<Integer, Long> getVertexLabelSupport(List<CachedGraph> input) {
    return super.getVertexLabelSupport(input)
      .entrySet()
      .stream()
      .map(e -> new Pair<>(db.decode(e.getKey()), e.getValue()))
      .flatMap(p -> {
        String child = p.getKey();

        int[] labels = new int[] {db.encode(child)};

        while (child.contains(DMGMConstants.Separators.TAXONOMY_PATH_LEVEL)) {
          String parent = StringUtils.substringBeforeLast(child, DMGMConstants.Separators.TAXONOMY_PATH_LEVEL);
          labels = ArrayUtils.add(labels, db.encode(parent));
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

  private Map<Integer, Pair<Integer, int[]>> getLabelTaxonomyPaths(Map<Integer, Long> vertexLabelSupport) {
    return vertexLabelSupport
      .keySet()
      .stream()
      .map(db::decode)
      .filter(s -> s.contains(DMGMConstants.Separators.TAXONOMY_PATH_LEVEL))
      .map(s -> {
        int[] ints = new int[] {db.encode(s)};

        while (s.contains(DMGMConstants.Separators.TAXONOMY_PATH_LEVEL)) {
          s = StringUtils.substringBeforeLast(s, DMGMConstants.Separators.TAXONOMY_PATH_LEVEL);
          ints = ArrayUtils.add(ints, db.encode(s));
        }

        ArrayUtils.reverse(ints);

        return ints;
      })
      .collect(Collectors.toMap(
        a -> a[a.length - 1],
        a -> new Pair<>(a[0], ArrayUtils.subarray(a, 1, a.length))));
  }
}
