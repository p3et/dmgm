package org.biiig.dmgm.impl.concurrency;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.concurrency.PartitionTaskFactory;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;

public class ConcurrencyUtil {

  public static <I, O> Collection<O> processQueue(
    Queue<I> queue, PartitionTaskFactory<I, O> processTaskFactory) {

    // create executor
    int parallelism = Runtime.getRuntime().availableProcessors();
    ExecutorService executor = Executors.newFixedThreadPool(parallelism);

    // init tasks
    List<RunnableFuture<O>> tasks = Lists.newArrayListWithExpectedSize(parallelism);
    for (int i = 0; i < parallelism; i++) {
      RunnableFuture<O> futureTask = processTaskFactory.create(queue);
      tasks.add(futureTask);
      executor.execute(futureTask);
    }

    // wait for tasks to finish
    Collection<O> partitions = Lists.newArrayListWithExpectedSize(parallelism);
    for (RunnableFuture<O> task : tasks) {
      try {
        partitions.add(task.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    return partitions;
  }
}
