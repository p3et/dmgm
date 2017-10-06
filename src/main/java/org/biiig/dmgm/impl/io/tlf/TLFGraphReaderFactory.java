package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.api.DMGraphDatabase;
import org.biiig.dmgm.api.io.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;

import java.util.Queue;

public class TLFGraphReaderFactory implements TLFSplitReaderFactory {

  private final DMGraphFactory graphFactory;
  private final DMGraphDatabase database;

  public TLFGraphReaderFactory(DMGraphFactory graphFactory, DMGraphDatabase database) {
    this.graphFactory = graphFactory;
    this.database = database;
  }

  @Override
  public TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFGraphReader(splits, reachedEOF, graphFactory, database);
  }
}
