package org.biiig.dmgm.impl.model.source.tlf;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.source.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;

import java.util.Queue;

public class TLFGraphReaderFactory implements TLFSplitReaderFactory {

  private final DMGraphFactory graphFactory;
  private final DMGraphCollection database;

  public TLFGraphReaderFactory(DMGraphFactory graphFactory, DMGraphCollection database) {
    this.graphFactory = graphFactory;
    this.database = database;
  }

  @Override
  public TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFGraphReader(splits, reachedEOF, graphFactory, database);
  }
}
