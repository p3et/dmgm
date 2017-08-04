package org.biiig.dmgm.impl;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.db.LabelDictionary;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase implements Database {
  private final List<DirectedGraph> graphs = new ArrayList<>();
  private LabelDictionary vertexDictionary;
  private LabelDictionary edgeDictionary;

  @Override
  public void setVertexDictionary(LabelDictionary dictionary) {
    this.vertexDictionary = dictionary;
  }

  @Override
  public void setEdgeDictionary(LabelDictionary dictionary) {
    this.edgeDictionary = dictionary;
  }
}
