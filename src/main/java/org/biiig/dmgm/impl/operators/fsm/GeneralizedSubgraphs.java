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

package org.biiig.dmgm.impl.operators.fsm;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.config.DmgmConstants;
import org.biiig.dmgm.api.db.SymbolDictionary;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.util.collectors.GroupByKeyListValues;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Mixin to create a generalized version of a subgraph mining algorithm.
 * @param <G> graph type
 * @param <S> support type
 */
interface GeneralizedSubgraphs<G extends WithGraph & WithTaxonomyPaths, S>
    extends SubgraphMining<G, S> {

  /**
   * Create an array of taxonomy paths where indices correspond to the vertex ids of the graph.
   *
   * @param graph graph
   * @param taxonomyPathIndex map: label -> taxonomy path
   * @return array of taxonomy paths
   */
  default int[][] getTaxonomyPaths(CachedGraph graph, Map<Integer, int[]> taxonomyPathIndex) {
    int[][] taxonomyPaths = new int[graph.getVertexCount()][];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++) {
      taxonomyPaths[vertexId] = taxonomyPathIndex.get(graph.getVertexLabel(vertexId));

    }
    return taxonomyPaths;
  }

  /**
   * Create an index of all vertex labels and their taxonomy path to the root.
   *
   * @param dictionary vertex label dictionary
   * @param input cached graph collection
   * @return map: child label -> parent label...
   */
  default Map<Integer, int[]> getTaxonomyPathIndex(
      SymbolDictionary dictionary, Collection<CachedGraph> input) {

    // get distinct bottom level labels
    int[] vertexLabels = getDistinctVertexLabels(input);

    // extract taxonomy
    Map<Integer, Integer> taxonomies = getVertexLabelTaxonomies(dictionary, vertexLabels);

    // get all generalizations
    IntStream generalizedLabelStream = taxonomies
        .values()
        .stream()
        .mapToInt(i -> i);

    // create index
    return IntStream.concat(IntStream.of(vertexLabels), generalizedLabelStream)
      .distinct()
      .mapToObj(key -> {
        int[] path = new int[]{key};
        int child = key;

        Integer parent;
        while ((parent = taxonomies.get(child)) != null) {
          path = ArrayUtils.add(path, parent);
          child = parent;
        }

        ArrayUtils.reverse(path);

        return new Pair<>(key, path);
      })
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  }

  /**
   * Get a map covering all vertex label taxonomies.
   * Note, that all vertex labels without an entry in the resulting map are roots of a taxonomy.
   *
   * @param dictionary vertex label dictionary
   * @param distinctVertexLabels array of bottom level vertex labels
   * @return map: child -> parent
   */
  default Map<Integer, Integer> getVertexLabelTaxonomies(
      SymbolDictionary dictionary, int[] distinctVertexLabels) {

    return IntStream.of(distinctVertexLabels)
      .mapToObj(dictionary::decode)
      .filter(s -> s.contains(DmgmConstants.Separators.TAXONOMY_PATH_LEVEL))
      .flatMap(child -> {
        Collection<Pair<String, String>> childParents = Lists.newArrayList();

        while (child.contains(DmgmConstants.Separators.TAXONOMY_PATH_LEVEL)) {
          String parent =
              StringUtils.substringBeforeLast(child, DmgmConstants.Separators.TAXONOMY_PATH_LEVEL);
          childParents.add(new Pair<>(child, parent));
          child = parent;
        }

        return childParents.stream();
      })
      .distinct()
      .collect(Collectors.toMap(
        p -> dictionary.encode(p.getKey()),
        p -> dictionary.encode(p.getValue())
      ));
  }

  /**
   * Get an array of distinct vertex labels.
   *
   * @param input cached graph collection
   * @return array of all vertex labels
   */
  default int[] getDistinctVertexLabels(Collection<CachedGraph> input) {
    return getParallelizableStream(input)
      .flatMapToInt(g -> IntStream.of(g.getVertexLabels()))
      .distinct()
      .toArray();
  }

  /**
   * Get all frequent specializations of a frequent top-level (most general) pattern.
   *
   * @param topLevel top-level pattern
   * @param embeddings the top-level pattern's embeddings
   * @param support top-level support
   * @param graphIndex map: graph id -> graph
   * @param minSupportAbsolute minimum support threshold
   * @return frequent specializations with support
   */
  default Stream<Pair<DfsCode, S>> getFrequentSpecializations(
      DfsCode topLevel, List<WithEmbedding> embeddings,
      S support, Map<Long, G> graphIndex, S minSupportAbsolute) {

    // init output
    List<Pair<DfsCode, S>> frequentSpecializations =
        Lists.newArrayList(new Pair<>(topLevel, support));

    // create a vector for each embedding
    int dimCount = topLevel.getVertexCount();

    List<SpecializableVector> vectors = Lists.newArrayList();

    List<SpecializableVector> finalVectors = vectors;
    BiConsumer<G, WithEmbedding> joinFunction = (wg, we) -> {
      DfsEmbedding embedding = we.getEmbedding();
      int vertexCount = embedding.getVertexCount();
      int[][] dimensionPaths = new int[vertexCount][];

      for (int time = 0; time < vertexCount; time++) {
        int vertexId = embedding.getVertexId(time);
        int[] dimensionPath = wg.getTaxonomyPath(vertexId);
        dimensionPaths[time] = dimensionPath;
      }

      // grow all children and add to list
      finalVectors.add(SpecializableVector.create(embedding, dimensionPaths));
    };

    joinAndExecute(embeddings, graphIndex, joinFunction);

    while (!vectors.isEmpty()) {
      Map<SpecializableVector, List<SpecializableVector>>
          aggregated = specializeVectors(vectors, dimCount)
          .collect(new GroupByKeyListValues<>(Function.identity(), Function.identity()));

      Stream<Pair<SpecializableVector, S>> frequent =
          addSupportAndFilter(aggregated, minSupportAbsolute, graphIndex, false);

      vectors = frequent
        .peek(p -> frequentSpecializations
            .add(new Pair<>(specializePattern(topLevel, p.getKey()), p.getValue())))
        .map(Pair::getKey)
        .map(aggregated::get)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    return frequentSpecializations.stream();
  }

  /**
   * Get a stream of specializations from a list of vectors.
   *
   * @param vectors parent vectors
   * @param dimCount number of vector fields (dimensions)
   *
   * @return all specializations of all vectors
   */
  default Stream<SpecializableVector> specializeVectors(
      List<SpecializableVector> vectors, int dimCount) {

    return vectors
      .stream()
      .flatMap(parent -> {
        SpecializableVector[] specializations = new SpecializableVector[0];

        for (int dim = parent.getLastSpecialization(); dim < dimCount; dim++) {
          SpecializableVector child = parent.getSpecialization(dim);
          if (child != null) {
            specializations = ArrayUtils.add(specializations, child);
          }
        }

        return Stream.of(specializations);
      });
  }

  /**
   * Get a DFS code specialized based on a specialized vector.
   *
   * @param topLevel top-level pattern
   * @param vector specialized vector
   *
   * @return specialized pattern
   */
  default DfsCode specializePattern(DfsCode topLevel, SpecializableVector vector) {

    int dimCount = vector.size();
    int[] specializedVertexLabels = new int[dimCount];

    for (int i = 0; i < dimCount; i++) {
      specializedVertexLabels[i] = vector.getSpecializedValue(i);
    }

    return new DfsCode(
      topLevel.getLabel(),
      specializedVertexLabels,
      topLevel.getEdgeLabels(),
      topLevel.getSourceIds(),
      topLevel.getTargetIds(),
      topLevel.getOutgoings()
    );
  }

}
