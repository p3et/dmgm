package org.biiig.dmgm.api;

import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.db.LabelDictionary;

import java.util.Collection;


public interface Database {
  void setVertexDictionary(LabelDictionary dictionary);

  void setEdgeDictionary(LabelDictionary dictionary);

  LabelDictionary getVertexDictionary();

  LabelDictionary getEdgeDictionary();

  int getGraphCount();

  void store(DirectedGraph graph);

  DirectedGraph getGraph(int graphId);
}
