package io.ddupg.garlic.pool.patrol;

import io.ddupg.garlic.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Patrolman implements Closeable {

  private final ScheduledExecutorService executor;

  public Patrolman() {
    executor = Executors.newScheduledThreadPool(1,
        new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("Garlic-Patrolman-%d")
            .build());
  }

  public void schedule(long interval, Runnable runner) {
    executor.scheduleAtFixedRate(runner, interval, interval, TimeUnit.MICROSECONDS);
  }

  @Override
  public void close() throws IOException {
    executor.shutdown();
  }
}
