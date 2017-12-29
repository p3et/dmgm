package org.biiig.dmgm.impl.algorithms.tfsm;

import de.jesemann.queue_stream.QueueStreamSource;
import de.jesemann.queue_stream.util.GroupByFunctionListValues;
import de.jesemann.queue_stream.util.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.cli.pattern_growth.GrowChildren;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.List;
import java.util.function.Consumer;

public class OutputAndGrowChildren implements Consumer<Pair<DFSCode, List<DFSEmbedding>>> {
  private final GraphCollection input;
  private final GraphCollection output;
  private final QueueStreamSource<Pair<DFSCode, List<DFSEmbedding>>> queue;
  private final int minSupportAbs;
  private final GrowChildren growChildren = new GrowChildren();

  public OutputAndGrowChildren(GraphCollection input, GraphCollection output,
                               QueueStreamSource<Pair<DFSCode, List<DFSEmbedding>>> queue, int minSupportAbs) {
    this.input = input;
    this.output = output;
    this.queue = queue;
    this.minSupportAbs = minSupportAbs;
  }

  @Override
  public void accept(Pair<DFSCode, List<DFSEmbedding>> pairs) {
    output(pairs);
    growChildren(pairs);
  }

  private void output(Pair<DFSCode, List<DFSEmbedding>> pairs) {
    output.store(pairs.getKey());
  }

  private void growChildren(Pair<DFSCode, List<DFSEmbedding>> parentEmbeddings) {

    DFSCode parentCode = parentEmbeddings.getKey();
    int[] rightmostPath = parentCode.getRightmostPath();

    parentEmbeddings
      .getValue()
      .stream()
      // group by graphId
      .collect(new GroupByFunctionListValues<>(DFSEmbedding::getGraphId))
      .entrySet()
      .stream()
      .flatMap(
        entry -> {
          IntGraph graph = input.getGraph(entry.getKey());
          return entry
            .getValue()
            .stream()
            .flatMap(
              embedding ->
                growChildren.apply(graph, parentCode, rightmostPath, embedding).stream());
        }
      )
      .collect(new GroupByKeyListValues<>())
      .entrySet()
      .stream()
      .map(e -> new Pair<>(e.getKey(), e.getValue()))
      .filter(new FilterFrequent(minSupportAbs))
      .forEach(queue::add);
  }

}
