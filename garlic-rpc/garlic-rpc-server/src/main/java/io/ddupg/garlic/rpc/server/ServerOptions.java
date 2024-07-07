package io.ddupg.garlic.rpc.server;

import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;

public class ServerOptions {

  private final int port;
  private int bossThreads = 1;
  private int workerThreads;

  private final SchedulerOptions schedulerOptions = new SchedulerOptions();

  public ServerOptions(int port) {
    this.port = port;
  }

  public int port() {
    return port;
  }

  public int bossThreads() {
    return bossThreads;
  }

  public ServerOptions bossThreads(int bossThreads) {
    Preconditions.checkArgument(bossThreads > 0);
    this.bossThreads = bossThreads;
    return this;
  }

  public int workerThreads() {
    return workerThreads;
  }

  public ServerOptions workerThreads(int workerThreads) {
    Preconditions.checkArgument(bossThreads > 0);
    this.workerThreads = workerThreads;
    return this;
  }

  public SchedulerOptions schedulerOptions() {
    return schedulerOptions;
  }
}
