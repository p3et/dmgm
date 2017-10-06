package org.biiig.dmgm.api;

import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.db.LabelDictionary;


public interface DMGraphDatabase {
  void setVertexDictionary(LabelDictionary dictionary);

  void setEdgeDictionary(LabelDictionary dictionary);

  LabelDictionary getVertexDictionary();

  LabelDictionary getEdgeDictionary();

  int getGraphCount();

  void store(DMGraph graph);

  DMGraph getGraph(int graphId);
}
