package org.biiig.dmgm.impl.concurrency;

import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DequeProcessor<I> implements Runnable {

  private final Deque<I> deque;
  private final AtomicInteger activeCount;
  private boolean active;

  public DequeProcessor(Deque<I> deque, AtomicInteger activeCount) {
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
    String x = activeCount.toString();

    if (this.active != active) {
      if (active) {
        activeCount.incrementAndGet();
      } else {
        activeCount.decrementAndGet();
      }

      System.out.println(x + " + " + active + "->" + activeCount);

      this.active = active;
    }
  }
}
