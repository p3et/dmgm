package org.biiig.dmgm.api.model.collection;

import org.biiig.dmgm.api.model.graph.DMGraph;


public interface DMGraphCollection {
  void setVertexDictionary(LabelDictionary dictionary);

  void setEdgeDictionary(LabelDictionary dictionary);

  LabelDictionary getVertexDictionary();

  LabelDictionary getEdgeDictionary();

  int getGraphCount();

  void store(DMGraph graph);

  DMGraph getGraph(int graphId);
}
