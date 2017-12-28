package org.biiig.dmgm.api.model.collection;

import org.biiig.dmgm.api.algorithms.tfsm.Operator;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.stream.Stream;


public interface GraphCollection extends Iterable<IntGraph> {
  GraphCollection withVertexDictionary(LabelDictionary dictionary);

  GraphCollection withEdgeDictionary(LabelDictionary dictionary);

  LabelDictionary getVertexDictionary();

  LabelDictionary getEdgeDictionary();

  int size();

  void store(IntGraph graph);

  IntGraph getGraph(int graphId);

  Stream<IntGraph> stream();

  Stream<IntGraph> parallelStream();

  default GraphCollection apply(Operator operator) {
    return operator.apply(this);
  }
}
