package org.biiig.dmgm.impl.model.collection;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.DMGraph;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryGraphCollection implements DMGraphCollection {
  private final AtomicInteger newGraphId = new AtomicInteger();
  private final Map<Integer, DMGraph> graphs = new ConcurrentHashMap<>();

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
  public void store(DMGraph graph) {
    int graphId = newGraphId.getAndIncrement();
    graphs.put(graphId, graph);
  }

  @Override
  public DMGraph getGraph(int graphId) {
    return graphs.get(graphId);
  }
}
