package org.biiig.dmgm.impl.model.collection;

import org.biiig.dmgm.api.algorithms.tfsm.Algorithm;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryGraphCollection implements GraphCollection {
  private final AtomicInteger newGraphId = new AtomicInteger();
  private final Map<Integer, IntGraph> graphs = new ConcurrentHashMap<>();

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
  public int size() {
    return graphs.size();
  }

  @Override
  public void store(IntGraph graph) {
    int graphId = newGraphId.getAndIncrement();
    graphs.put(graphId, graph);
  }

  @Override
  public IntGraph getGraph(int graphId) {
    return graphs.get(graphId);
  }

  @Override
  public GraphCollection apply(Algorithm algorithm) {
    return null;
  }

  @Override
  public Iterator<IntGraph> iterator() {
    return new InMemoryGraphCollectionIterator(graphs);
  }
}
