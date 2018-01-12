package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import de.jesemann.paralleasy.recursion.Children;
import de.jesemann.paralleasy.recursion.Output;
import de.jesemann.paralleasy.recursion.RecursionStep;
import javafx.util.Pair;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessDFSNode implements RecursionStep<DFSCodeEmbeddingsPair, Consumer<GraphCollection>> {


  private final GraphCollection input;
  private final FilterOrOutput<DFSCodeEmbeddingsPair> filterOrOutput;
  private final int maxEdgeCount;

  ProcessDFSNode(GraphCollection input, FilterOrOutput<DFSCodeEmbeddingsPair> filterOrOutput, int maxEdgeCount) {
    this.input = input;
    this.filterOrOutput = filterOrOutput;
    this.maxEdgeCount = maxEdgeCount;
  }

  @Override
  public void process(DFSCodeEmbeddingsPair pair, Children<DFSCodeEmbeddingsPair> children, Output<Consumer<GraphCollection>> output) {
    DFSCode parentCode = pair.getDFSCode();

    int[] rightmostPath = parentCode.getRightmostPath();

    List<Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphCollection>>>> grown = pair
      .getEmbeddings()
      .stream()
      .collect(new GroupByGraphIdListEmbeddings())
      .entrySet()
      .stream()
      .flatMap(
        entry -> {
          Graph graph = input.getGraph(entry.getKey());
          return entry
            .getValue()
            .stream()
            .flatMap(
              embedding ->
                Stream.of(new GrowAllChildren().apply(graph, parentCode, rightmostPath, embedding)));
        })
      .collect(new GroupByDFSCodeListEmbeddings())
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .filter(new IsMinimal())
      .map(filterOrOutput::apply)
      .collect(Collectors.toList());
    
    grown
      .stream()
      .map(Pair::getKey)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .filter(p -> p.getDFSCode().getEdgeCount() < maxEdgeCount)
      .forEach(children::add);

    grown
      .stream()
      .map(Pair::getValue)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .forEach(output::add);
  }
}
