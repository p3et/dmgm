package org.biiig.dmgm.impl.model.collection;

import org.biiig.dmgm.api.model.graph.DMGraph;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryGraphCollectionIterator implements Iterator<DMGraph> {
  private final AtomicInteger id = new AtomicInteger(0);
  private final Map<Integer, DMGraph> graphs;
  private final int maxId;

  public InMemoryGraphCollectionIterator(Map<Integer, DMGraph> graphs) {
    this.graphs = graphs;
    this.maxId = graphs.size() - 1;
  }

  @Override
  public boolean hasNext() {
    return id.get() <= maxId;
  }

  @Override
  public DMGraph next() {
    return graphs.get(id.getAndIncrement());
  }
}
