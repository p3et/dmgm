package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import de.jesemann.paralleasy.recursion.Children;
import de.jesemann.paralleasy.recursion.Output;
import de.jesemann.paralleasy.recursion.RecursionStep;
import javafx.util.Pair;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Specialize
  implements RecursionStep<PatternVectorsPair, Consumer<HyperVertexDB>> {


  private final AllSpecializations allSpecializations;
  private final DFSCode dfsCode;
  private final FilterOrOutput<PatternVectorsPair> filter;

  public Specialize(DFSCode dfsCode, FilterOrOutput<PatternVectorsPair> filter) {
    this.dfsCode = dfsCode;
    this.filter = filter;
    allSpecializations = new AllSpecializations(this.dfsCode.getVertexCount());
  }

  @Override
  public void process(
    PatternVectorsPair parent,
    Children<PatternVectorsPair> children,
    Output<Consumer<HyperVertexDB>> output
  ) {

    List<Pair<Optional<PatternVectorsPair>, Optional<Consumer<HyperVertexDB>>>> childPairs = parent
      .getVectors()
      .stream()
      .flatMap(allSpecializations)
      .collect(new GroupByKeyListValues<>(Function.identity(), Function.identity()))
      .entrySet()
      .stream()
      .map(e -> new PatternVectorsPair(specialize(dfsCode, e.getKey()), e.getValue()))
      .map(filter::apply)
      .collect(Collectors.toList());

    childPairs
      .stream()
      .map(Pair::getKey)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .forEach(children::add);

    childPairs
      .stream()
      .map(Pair::getValue)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .forEach(output::add);
  }

  private DFSCode specialize(DFSCode parent, MultiDimensionalVector vector) {
    int[] vertexLabels = parent
      .vertexIdStream()
      .map(dim -> vector
        .getSpecializedValue(dim)
        .orElse(parent.getVertexLabel(dim)))
      .toArray();

    return new DFSCode(
      parent.getLabel(),
      vertexLabels,
      parent.getVertexLabels(),
      parent.getSourceIds(),
      parent.getTargetIds(),
      parent.getOutgoings()
    );
  }
}
