package org.biiig.dmgm.api.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by peet on 16.08.17.
 */
public interface TaskWithOutputFactory<T> {
  TaskWithOutput<T> create(AtomicInteger activeCount);
}
