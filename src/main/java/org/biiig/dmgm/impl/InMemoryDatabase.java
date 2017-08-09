package org.biiig.dmgm.impl;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.impl.db.LabelDictionary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDatabase implements Database {
  private final AtomicInteger newGraphId = new AtomicInteger();
  private final Map<Integer, DirectedGraph> graphs = new ConcurrentHashMap<>();
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

  @Override
  public LabelDictionary getVertexDictionary() {
    return vertexDictionary;
  }

  @Override
  public LabelDictionary getEdgeDictionary() {
    return edgeDictionary;
  }

  @Override
  public int getGraphCount() {
    return graphs.size();
  }

  @Override
  public void store(DirectedGraph graph) {
    graphs.put(newGraphId.getAndIncrement(), graph);
  }
}
