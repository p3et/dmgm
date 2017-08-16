package org.biiig.dmgm.api.concurrency;

public interface TaskWithOutput<O> extends Runnable {
  O getOutput();
}
