package org.biiig.dmgm.impl.model.source.tlf;

import org.biiig.dmgm.api.model.collection.IntGraphCollection;
import org.biiig.dmgm.api.model.source.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;

import java.util.Queue;

public class TLFGraphReaderFactory implements TLFSplitReaderFactory {

  private final IntGraphFactory graphFactory;
  private final IntGraphCollection database;

  public TLFGraphReaderFactory(IntGraphFactory graphFactory, IntGraphCollection database) {
    this.graphFactory = graphFactory;
    this.database = database;
  }

  @Override
  public TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFGraphReader(splits, reachedEOF, graphFactory, database);
  }
}
