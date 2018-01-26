package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SingleEdgeDFSNodes implements Runnable {
  private final GraphCollection input;
  private final FilterOrOutput<DFSCodeEmbeddingsPair> filterOrOutput;
  private Collection<Consumer<GraphCollection>> output;
  private Collection<DFSCodeEmbeddingsPair> singleEdgeDFSNodes;

  public SingleEdgeDFSNodes(GraphCollection input, FilterOrOutput<DFSCodeEmbeddingsPair> filterOrOutput) {
    this.input = input;
    this.filterOrOutput = filterOrOutput;
  }

  public Collection<Consumer<GraphCollection>> getOutput() {
    return output;
  }

  public Collection<DFSCodeEmbeddingsPair> getSingleEdgeDFSNodes() {
    return singleEdgeDFSNodes;
  }


  @Override
  public void run() {
    List<Pair<Optional<DFSCodeEmbeddingsPair>, Optional<Consumer<GraphCollection>>>> initalized =
      input
        .stream()
        .flatMap(new InitializeParents())
        .collect(new GroupByDFSCodeListEmbeddings())
        .entrySet()
        .stream()
        .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
        .map(filterOrOutput::apply)
        .collect(Collectors.toList());

    singleEdgeDFSNodes = initalized
      .stream()
      .map(Pair::getKey)
      .filter(Optional::isPresent)
      .map(Optional::get)
//      .peek(c -> System.out.println(Thread.currentThread().getId() + ":" + c.getDFSCode()))
      .collect(Collectors.toList());

    output = initalized
      .stream()
      .map(Pair::getValue)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
  }
}
