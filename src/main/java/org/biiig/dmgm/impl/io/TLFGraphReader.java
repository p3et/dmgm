package org.biiig.dmgm.impl.io;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.DirectedGraph;
import org.biiig.dmgm.api.model.DirectedGraphFactory;

import java.util.List;
import java.util.Queue;

public class TLFGraphReader implements Runnable {
  private final List<DirectedGraph> graphs = Lists.newArrayList();
  private final Queue<String[]> splits;
  private final Database database;
  private final boolean readingComplete;
  private final DirectedGraphFactory graphFactory;
  private boolean run = true;

  public TLFGraphReader(Queue<String[]> splits, DirectedGraphFactory graphFactory, Database database,
    boolean readingComplete) {
    this.splits = splits;
    this.graphFactory = graphFactory;
    this.database = database;
    this.readingComplete = readingComplete;
  }

  @Override
  public void run() {
    while (run) {
      if (splits.isEmpty()) {
        if (readingComplete) {
          run = false;
        } else {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      } else {
        String[] split = splits.poll();

        if (split != null) {
          System.out.println(split.length);
        }
      }
    }
  }
}
