package org.biiig.dmgm.impl.algorithms.tfsm;

import de.jesemann.queue_stream.QueueStreamSource;
import javafx.util.Pair;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GrowChildren implements Function<Pair<DFSCode, List<DFSEmbedding>>, Stream<IntGraph>> {
  public GrowChildren(GraphCollection graphCollection, QueueStreamSource<Pair<DFSCode, List<DFSEmbedding>>> queueStreamSource) {
  }




//  private void growChildren(DFSCode parentCode, List<DFSEmbedding> parentEmbeddings) {
//    parentEmbeddings
//      .stream()
//      .flatMap(new GrowChildren(graphCollection, queueStreamSource))
//      .collect(Collectors.groupingByConcurrent(Pair::getKey, Collectors.toList()))
//      .entrySet()
//      .stream()
//      .map(e -> new Pair<>(
//        e.getKey(),
//        e.getValue()
//          .stream()
//          .map(Pair::getValue)
//          .collect(Collectors.toList()))
//      )
//      .forEach(queueStreamSource::add);
//  }

  @Override
  public Stream<IntGraph> apply(Pair<DFSCode, List<DFSEmbedding>> parentPair) {
    return Stream.of(parentPair.getKey());
  }
}
