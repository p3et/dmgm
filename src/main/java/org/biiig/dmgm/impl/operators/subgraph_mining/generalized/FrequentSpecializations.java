package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.AggregateAndFilter;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FrequentSpecializations
  implements Function<Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>>, Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>>>> {

  private final Map<Long, int[][]> graphDimensionPaths;
  private final AggregateAndFilter aggregateAndFilter;

  public FrequentSpecializations(Map<Long, int[][]> graphDimensionPaths, AggregateAndFilter aggregateAndFilter) {
    this.graphDimensionPaths = graphDimensionPaths;
    this.aggregateAndFilter = aggregateAndFilter;
  }

  @Override
  public Stream<Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>>> apply(Pair<Pair<DFSCode,List<DFSEmbedding>>, Map<Integer, Long>> input) {
    List<Pair<Pair<DFSCode, List<DFSEmbedding>>, Map<Integer, Long>>> frequentSpecializations = Lists.newArrayList(input);


    DFSCode topLevel = input.getKey().getKey();
    int dimCount = topLevel.getVertexCount();

    Stream<MultiDimensionalVector> parents = initVectors(input);

    Stream<Pair<MultiDimensionalVector, MultiDimensionalVector>> children = specialize(parents, dimCount);

    List<Pair<Pair<MultiDimensionalVector, List<MultiDimensionalVector>>, Map<Integer, Long>>> frequent = aggregateAndFilter
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

      frequent = aggregateAndFilter
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

  public Stream<MultiDimensionalVector> initVectors(Pair<Pair<DFSCode, List<DFSEmbedding>>, Map<Integer, Long>> input) {
    return input
      .getKey()
      .getValue()
      .stream()
      .map(e -> {
        int[][] dimensionPaths = new int[e.getVertexCount()][];

        for (int time = 0; time < e.getVertexCount(); time++) {
          dimensionPaths[time] = graphDimensionPaths.get(e.getGraphId())[e.getVertexId(time)];
        }

        return MultiDimensionalVector.create(e, dimensionPaths);
      });
  }
}
