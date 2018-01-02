package org.biiig.dmgm.impl.algorithms.tfsm;

import de.jesemann.paralleasy.queue_stream.QueueStreamSource;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class OutputAndGrowChildren implements Consumer<DFSCodeEmbeddingsPair> {
  private final GraphCollection input;
  private final GraphCollection output;
  private final QueueStreamSource<DFSCodeEmbeddingsPair> queue;
  private final int minSupportAbs;
  private final GrowAllChildren growAllChildren = new GrowAllChildren();

  public OutputAndGrowChildren(GraphCollection input, GraphCollection output,
                               QueueStreamSource<DFSCodeEmbeddingsPair> queue, int minSupportAbs) {
    this.input = input;
    this.output = output;
    this.queue = queue;
    this.minSupportAbs = minSupportAbs;
  }

  @Override
  public void accept(DFSCodeEmbeddingsPair pairs) {
    output(pairs);
    growChildren(pairs);
  }

  private void output(DFSCodeEmbeddingsPair pairs) {
    output.add(pairs.getDfsCode());
  }

  private void growChildren(DFSCodeEmbeddingsPair parentEmbeddings) {

    DFSCode parentCode = parentEmbeddings.getDfsCode();

    System.out.println(Thread.currentThread().getId() + "\t" + parentCode);

    int[] rightmostPath = parentCode.getRightmostPath();

    Stream.of(
      parentEmbeddings
      .getEmbeddings())
      // group by graphId
      .collect(new GroupByGraphIdArrayEmbeddings())
      .entrySet()
      .stream()
      .flatMap(
        entry -> {
          Graph graph = input.getGraph(entry.getKey());
          return Stream.of(entry.getValue())
            .flatMap(
              embedding ->
                Stream.of(growAllChildren.apply(graph, parentCode, rightmostPath, embedding)));
        }
      )
      .collect(new GroupByDFSCodeArrayEmbeddings())
      .entrySet()
      .stream()
      .map(e -> new DFSCodeEmbeddingsPair(e.getKey(), e.getValue()))
      .filter(new IsMinimal())
      .filter(new FilterFrequent(minSupportAbs))
      .forEach(queue::add);
  }

}
