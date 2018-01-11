package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import de.jesemann.paralleasy.recursion.Children;
import de.jesemann.paralleasy.recursion.Output;
import de.jesemann.paralleasy.recursion.RecursionStep;
import javafx.util.Pair;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph.GraphBase;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Specialize
  implements RecursionStep<MultiDimensionalVector, Consumer<GraphCollection>> {


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
    MultiDimensionalVector parent,
    Children<MultiDimensionalVector> children,
    Output<Consumer<GraphCollection>> output
  ) {

    List<Pair<Optional<PatternVectorsPair>, Optional<Consumer<GraphCollection>>>> childPairs = allSpecializations
      .apply(parent)
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
      .map(PatternVectorsPair::getVectors)
      .flatMap(Collection::stream)
      .forEach(children::add);

    childPairs
      .stream()
      .map(Pair::getValue)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .forEach(output::add);
  }

  private Graph specialize(Graph dfsCode, MultiDimensionalVector vector) {
    Graph graph = new GraphBase();

    dfsCode
      .vertexIdStream()
      .forEach(dim -> graph
        .addVertex(
          vector.getSpecializedValue(dim)
            .orElse(dfsCode.getVertexLabel(dim))
        ));

    dfsCode
      .edgeIdStream()
      .forEach(e ->
        graph.addEdge(
          dfsCode.getSourceId(e),
          dfsCode.getTargetId(e),
          dfsCode.getEdgeLabel(e)
        ));

    return graph;
  }
}
