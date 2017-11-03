package org.biiig.dmgm.impl.concurrency;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DequeUpdateTask<I> implements Runnable {

  private final Deque<I> deque;
  private final AtomicInteger activeCount;
  private boolean active;

  public DequeUpdateTask(Deque<I> deque, AtomicInteger activeCount) {
    this.deque = deque;
    this.activeCount = activeCount;
  }

  @Override
  public void run() {
    setActive(true);

    while (!(deque.isEmpty() && activeCount.get() == 0)) {
      I next = deque.pollLast();

      if (next != null) {
        setActive(true);
        deque.addAll(process(next));
      } else {
        setActive(false);
      }
    }
  }

  protected abstract Collection<I> process(I next);

  private void setActive(boolean active) {
    if (this.active != active) {
      if (active) {
        activeCount.incrementAndGet();
      } else {
        activeCount.decrementAndGet();
      }

      this.active = active;
    }
  }
}
