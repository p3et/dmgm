package org.biiig.dmgm.impl.db;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.EPGMDatabase;

import java.util.Collection;
import java.util.stream.Stream;

public class InMemoryGraphCollection implements GraphCollection {

  private final EPGMDatabase db;
  private final Collection<SmallGraph> graphs;

  public InMemoryGraphCollection(EPGMDatabase db, Collection<SmallGraph> graphs) {
    this.db = db;
    this.graphs = graphs;
  }


  @Override
  public int size() {
    return graphs.size();
  }

  @Override
  public Stream<SmallGraph> stream() {
    return graphs.stream();
  }

  @Override
  public Stream<SmallGraph> parallelStream() {
    return graphs.parallelStream();
  }

  @Override
  public EPGMDatabase getDatabase() {
    return db;
  }
}
