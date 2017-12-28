package org.biiig.dmgm.api.model.collection;

import org.biiig.dmgm.api.algorithms.tfsm.Operator;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.stream.Stream;


public interface IntGraphCollection extends Iterable<IntGraph> {
  IntGraphCollection withVertexDictionary(LabelDictionary dictionary);

  IntGraphCollection withEdgeDictionary(LabelDictionary dictionary);

  LabelDictionary getVertexDictionary();

  LabelDictionary getEdgeDictionary();

  int size();

  void store(IntGraph graph);

  IntGraph getGraph(int graphId);

  IntGraphCollection apply(Operator operator);

  Stream<IntGraph> stream();
}
