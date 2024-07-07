package io.ddupg.garlic.rpc.server;

import io.ddupg.garlic.rpc.protocol.BaseProtos;
import io.ddupg.garlic.shaded.com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class Scheduler {

  private final SchedulerOptions options;
  private List<QueueExecutor> queueExecutors;

  public Scheduler(SchedulerOptions options) {
    this.options = options;
    queueExecutors = Lists.newArrayListWithCapacity(options.queueNums());
  }

  public CompletableFuture<BaseProtos.Response> schedule(ServerCall call) {
    CompletableFuture<BaseProtos.Response> future = new CompletableFuture<>();
    call.future = future;
    QueueExecutor executor = nextQueueExecutor();
    executor.enqueue(call);
    return future;
  }

  private QueueExecutor nextQueueExecutor() {
    if (queueExecutors.size() == 1) {
      return queueExecutors.get(0);
    } else {
      return queueExecutors.get(ThreadLocalRandom.current().nextInt(queueExecutors.size()));
    }
  }

}
