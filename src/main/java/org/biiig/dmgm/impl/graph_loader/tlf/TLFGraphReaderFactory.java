package org.biiig.dmgm.impl.graph_loader.tlf;

import org.biiig.dmgm.api.GraphCollection;

import java.util.Queue;

public class TLFGraphReaderFactory implements TLFSplitReaderFactory {

  private final GraphFactory graphFactory;
  private final GraphCollection database;

  public TLFGraphReaderFactory(GraphFactory graphFactory, GraphCollection database) {
    this.graphFactory = graphFactory;
    this.database = database;
  }

  @Override
  public TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFGraphReader(splits, reachedEOF, graphFactory, database);
  }
}
