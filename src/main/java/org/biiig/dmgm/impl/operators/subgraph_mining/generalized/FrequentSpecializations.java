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

package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.fsm.generalized.MultiDimensionalVector;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportSpecialization;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrequentSpecializations<S>
  implements Function<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>, Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>>> {

  private final SupportSpecialization<S> supportSpecialization;
  private final Map<Long, SpecializableCachedGraph> indexedGraphs;

  public FrequentSpecializations(SupportSpecialization<S> supportSpecialization, Map<Long, SpecializableCachedGraph> indexedGraphs) {
    this.supportSpecialization = supportSpecialization;
    this.indexedGraphs = indexedGraphs;
  }

  @Override
  public Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, S>> apply(Pair<Pair<DFSCode,List<DFSEmbedding>>, S> input) {
    List<Pair<Pair<DFSCode, List<DFSEmbedding>>, S>> frequentSpecializations = Lists.newArrayList(input);

    DFSCode topLevel = input.getKey().getKey();

    int dimCount = topLevel.getVertexCount();
    Stream<MultiDimensionalVector> parents = initVectors(input);

    Stream<Pair<MultiDimensionalVector, MultiDimensionalVector>> children = specialize(parents, dimCount);

    List<Pair<Pair<MultiDimensionalVector, List<MultiDimensionalVector>>, S>> frequent = supportSpecialization
      .aggregateAndFilter(children)
      .collect(Collectors.toList());

    while (!frequent.isEmpty()) {
      frequent.forEach(p -> {

        int[] specializedVertexLabels = new int[dimCount];

        for (int i = 0; i < dimCount; i++)
          specializedVertexLabels[i] = p.getKey().getKey().getSpecializedValue(i);

        DFSCode specialized = new DFSCode(
          topLevel.getLabel(),
          specializedVertexLabels,
          topLevel.getEdgeLabels(),
          topLevel.getSourceIds(),
          topLevel.getTargetIds(),
          topLevel.getOutgoings()
        );

        List<DFSEmbedding> embeddings = p
          .getKey()
          .getValue()
          .stream()
          .map(MultiDimensionalVector::getEmbedding)
          .collect(Collectors.toList());

        frequentSpecializations.add(new Pair<>(new Pair<>(specialized, embeddings), p.getValue()));
      });

      parents = frequent
        .stream()
        .map(Pair::getKey)
        .map(Pair::getValue)
        .flatMap(Collection::stream);

      children = specialize(parents, dimCount);

      frequent = supportSpecialization
        .aggregateAndFilter(children)
        .collect(Collectors.toList());
    }


    return frequentSpecializations.stream();
  }

  public Stream<Pair<MultiDimensionalVector, MultiDimensionalVector>> specialize(Stream<MultiDimensionalVector> parents, int dimCount) {
    return parents
      .flatMap(parent -> {
        MultiDimensionalVector[] specializations = new MultiDimensionalVector[0];

        for (int dim = parent.getLastSpecialization(); dim < dimCount; dim++) {
          MultiDimensionalVector child = parent.getSpecialization(dim);
          if (child != null)
            specializations = ArrayUtils.add(specializations, child);
        }

        return Stream.of(specializations);
      })
      .map(v -> new Pair<>(v, v));
  }

  public Stream<MultiDimensionalVector> initVectors(Pair<Pair<DFSCode, List<DFSEmbedding>>, S> input) {

    return input
      .getKey()
      .getValue()
      .stream()
      .map(e -> {
        int[][] dimensionPaths = new int[e.getVertexCount()][];

        for (int time = 0; time < e.getVertexCount(); time++) {
          SpecializableCachedGraph graph = indexedGraphs.get(e.getGraphId());
          int vertexId = e.getVertexId(time);
          int vertexLabel = graph.getVertexLabel(vertexId);
          int[] taxonomyTail = graph.getTaxonomyTail(vertexId);
          int[] dimensionPath = new int[]{vertexLabel};

          if (taxonomyTail != null)
            dimensionPath = ArrayUtils.addAll(dimensionPath, taxonomyTail);

          dimensionPaths[time] = dimensionPath;
        }

        MultiDimensionalVector vector = MultiDimensionalVector.create(e, dimensionPaths);

        return vector;
      });
  }
}
