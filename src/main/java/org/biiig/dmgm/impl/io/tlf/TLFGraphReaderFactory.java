package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.api.model.graph.DirectedGraphFactory;

import java.util.Queue;

public class TLFGraphReaderFactory implements TLFSplitReaderFactory {

  private final DirectedGraphFactory graphFactory;
  private final Database database;

  public TLFGraphReaderFactory(DirectedGraphFactory graphFactory, Database database) {
    this.graphFactory = graphFactory;
    this.database = database;
  }

  @Override
  public TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFGraphReader(splits, reachedEOF, graphFactory, database);
  }
}
