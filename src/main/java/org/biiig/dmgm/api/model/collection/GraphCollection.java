package org.biiig.dmgm.api.model.collection;

import org.biiig.dmgm.api.algorithms.tfsm.Algorithm;
import org.biiig.dmgm.api.model.graph.IntGraph;


public interface GraphCollection extends Iterable<IntGraph> {
  void setVertexDictionary(LabelDictionary dictionary);

  void setEdgeDictionary(LabelDictionary dictionary);

  LabelDictionary getVertexDictionary();

  LabelDictionary getEdgeDictionary();

  int size();

  void store(IntGraph graph);

  IntGraph getGraph(int graphId);

  GraphCollection apply(Algorithm algorithm);
}