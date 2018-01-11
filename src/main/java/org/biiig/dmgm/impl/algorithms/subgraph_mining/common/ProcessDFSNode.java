package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.recursion.Children;
import de.jesemann.paralleasy.recursion.Output;
import de.jesemann.paralleasy.recursion.RecursionStep;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class ProcessDFSNode implements RecursionStep<DFSCodeEmbeddingsPair, Graph> {


  private final GraphCollection input;
  private final Predicate<DFSCodeEmbeddingsPair> filterAndOutput;
  private final int maxEdgeCount;

  ProcessDFSNode(GraphCollection input, Predicate<DFSCodeEmbeddingsPair> filterAndOutput, int maxEdgeCount) {
    this.input = input;
    this.filterAndOutput = filterAndOutput;
    this.maxEdgeCount = maxEdgeCount;
  }

  @Override
  public void process(DFSCodeEmbeddingsPair pair, Children<DFSCodeEmbeddingsPair> children, Output<Graph> output) {
    DFSCode parentCode = pair.getDfsCode();

    int[] rightmostPath = parentCode.getRightmostPath();

    Stream.of(pair.getEmbeddings())
      .collect(new GroupByGraphIdArrayEmbeddings())
      .entrySet()
      .stream()
      .flatMap(
        entry -> {
          Graph graph = input.getGraph(entry.getKey());
          return Stream.of(entry.getValue())
            .flatMap(
              embedding ->
                Stream.of(new GrowAllChildren().apply(graph, parentCode, rightmostPath, embedding)));
        })
      .collect(new GroupByDFSCodeArrayEmbeddings())
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .filter(new IsMinimal())
      .filter(filterAndOutput)
      .filter(p -> p.getDfsCode().getEdgeCount() < maxEdgeCount)
      .peek(c -> System.out.println(Thread.currentThread().getId() + ":" + c.getDfsCode()))
      .forEach(children::add);
  }
}
