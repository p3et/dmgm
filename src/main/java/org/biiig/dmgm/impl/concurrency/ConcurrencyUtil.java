package org.biiig.dmgm.impl.concurrency;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.concurrency.TaskWithOutputFactory;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyUtil {

  public static <OUT> Collection<OUT> runParallel(TaskWithOutputFactory<OUT> taskFactory) {

    // create parallel environment
    int parallelism = Runtime.getRuntime().availableProcessors();
    AtomicInteger activeCount = new AtomicInteger(0);
    ExecutorService executor = Executors.newFixedThreadPool(parallelism);

    // init tasks
    List<TaskWithOutput<OUT>> tasks = Lists.newArrayListWithExpectedSize(parallelism);
    TaskWithOutput<OUT> mainTask = taskFactory.create(activeCount);
    tasks.add(mainTask);

    for (int i = 1; i < parallelism; i++) {
      TaskWithOutput<OUT> task = taskFactory.create(activeCount);
      tasks.add(task);
      executor.execute(task);
    }

    mainTask.run();

    // collect outputs
    Collection<OUT> output = Lists.newArrayListWithExpectedSize(parallelism);
    for (TaskWithOutput<OUT> task : tasks) {
      output.add(task.getOutput());
    }

    return output;
  }
}
