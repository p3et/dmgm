package org.biiig.dmgm.impl.model.collection;

import org.biiig.dmgm.api.algorithms.tfsm.Operator;
import org.biiig.dmgm.api.model.collection.IntGraphCollection;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class InMemoryGraphCollection implements IntGraphCollection {

  private final AtomicInteger newGraphId = new AtomicInteger();
  private final Map<Integer, IntGraph> graphs = new ConcurrentHashMap<>();

  private LabelDictionary vertexDictionary;
  private LabelDictionary edgeDictionary;

  @Override
  public IntGraphCollection withVertexDictionary(LabelDictionary dictionary) {
    this.vertexDictionary = dictionary;
    return this;
  }

  @Override
  public IntGraphCollection withEdgeDictionary(LabelDictionary dictionary) {
    this.edgeDictionary = dictionary;
    return this;
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
  public IntGraphCollection apply(Operator operator) {
    return null;
  }

  @Override
  public Stream<IntGraph> stream() {
    return graphs.values().stream();
  }

  @Override
  public Iterator<IntGraph> iterator() {
    return new InMemoryGraphCollectionIterator(graphs);
  }
}
