package org.biiig.dmgm.api.concurrency;

import java.util.Queue;
import java.util.concurrent.RunnableFuture;

public interface PartitionTaskFactory<I, O> {
  RunnableFuture<O> create(Queue<I> queue);
}
