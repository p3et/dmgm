package org.biiig.dmgm.impl.model.collection;

import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryGraphCollectionIterator implements Iterator<IntGraph> {
  private final AtomicInteger id = new AtomicInteger(0);
  private final Map<Integer, IntGraph> graphs;
  private final int maxId;

  public InMemoryGraphCollectionIterator(Map<Integer, IntGraph> graphs) {
    this.graphs = graphs;
    this.maxId = graphs.size() - 1;
  }

  @Override
  public boolean hasNext() {
    return id.get() <= maxId;
  }

  @Override
  public IntGraph next() {
    return graphs.get(id.getAndIncrement());
  }
}
