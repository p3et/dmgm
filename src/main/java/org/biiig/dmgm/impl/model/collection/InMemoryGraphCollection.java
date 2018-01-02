package org.biiig.dmgm.impl.model.collection;

import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class InMemoryGraphCollection implements GraphCollection {

  private final AtomicInteger newGraphId = new AtomicInteger();
  private final Map<Integer, IntGraph> graphs = new ConcurrentHashMap<>();

  private LabelDictionary vertexDictionary;
  private LabelDictionary edgeDictionary;

  @Override
  public GraphCollection withVertexDictionary(LabelDictionary dictionary) {
    this.vertexDictionary = dictionary;
    return this;
  }

  @Override
  public GraphCollection withEdgeDictionary(LabelDictionary dictionary) {
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
  public void add(IntGraph graph) {
    int graphId = newGraphId.getAndIncrement();
    graph.setId(graphId);
    graphs.put(graphId, graph);
  }

  @Override
  public IntGraph getGraph(int graphId) {
    return graphs.get(graphId);
  }

  @Override
  public Stream<IntGraph> stream() {
    return graphs.values().stream();
  }

  @Override
  public Stream<IntGraph> parallelStream() {
    return graphs.values().parallelStream();
  }

  @Override
  public Iterator<IntGraph> iterator() {
    return new InMemoryGraphCollectionIterator(graphs);
  }


}
