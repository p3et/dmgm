package org.biiig.dmgm.impl.graph_collection;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.Graph;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class InMemoryGraphCollection implements GraphCollection {

  private final AtomicInteger newGraphId = new AtomicInteger();
  private final Map<Integer, Graph> graphs = new ConcurrentHashMap<>();

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
  public void add(Graph graph) {
    int graphId = newGraphId.getAndIncrement();
    graph.setId(graphId);
    graphs.put(graphId, graph);
  }

  @Override
  public Graph getGraph(int graphId) {
    return graphs.get(graphId);
  }

  @Override
  public Stream<Graph> stream() {
    return graphs.values().stream();
  }

  @Override
  public Stream<Graph> parallelStream() {
    return graphs.values().parallelStream();
  }

  @Override
  public Iterator<Graph> iterator() {
    return new InMemoryGraphCollectionIterator(graphs);
  }

  public class InMemoryGraphCollectionIterator implements Iterator<Graph> {
    private final AtomicInteger id = new AtomicInteger(0);
    private final Map<Integer, Graph> graphs;
    private final int maxId;

    public InMemoryGraphCollectionIterator(Map<Integer, Graph> graphs) {
      this.graphs = graphs;
      this.maxId = graphs.size() - 1;
    }

    @Override
    public boolean hasNext() {
      return id.get() <= maxId;
    }

    @Override
    public Graph next() {
      return graphs.get(id.getAndIncrement());
    }
  }


}
