package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraphFactory;

import java.util.Queue;

public class TLFGraphReader extends TLFSplitReader {
  private final Database database;
  private final DirectedGraphFactory graphFactory;

  public TLFGraphReader(Queue<String[]> splits, boolean reachedEOF,
    DirectedGraphFactory graphFactory, Database database) {
    super(splits, reachedEOF);
    this.graphFactory = graphFactory;
    this.database = database;
  }



  @Override
  protected void process(String[] split) {
    System.out.println("Parse Graph");
  }

  @Override
  protected void finish() {

  }
}
