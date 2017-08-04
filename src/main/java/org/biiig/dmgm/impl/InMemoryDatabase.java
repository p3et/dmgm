package org.biiig.dmgm.impl;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.db.LabelDictionary;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase implements Database {
  private final List<DirectedGraph> graphs = new ArrayList<>();
  private LabelDictionary vertexDictionary;

  @Override
  public void setVertexDictionary(LabelDictionary vertexDictionary) {
    this.vertexDictionary = vertexDictionary;
  }
}
