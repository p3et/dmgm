package org.biiig.dmgm.impl.graph_loader.tlf;

import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public abstract class TLFSplitReader implements Runnable {
  private final Queue<String[]> splits;
  private final boolean reachedEOF;
  private boolean run = true;

  public TLFSplitReader(Queue<String[]> splits, boolean reachedEOF) {
    this.reachedEOF = reachedEOF;
    this.splits = splits;
  }

  @Override
  public void run() {
    while (run) {
      if (splits.isEmpty()) {
        if (reachedEOF) {
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
          process(split);
        }
      }
    }
    finish();
  }

  protected void finish() {
  }

  protected abstract void process(String[] split);
}
