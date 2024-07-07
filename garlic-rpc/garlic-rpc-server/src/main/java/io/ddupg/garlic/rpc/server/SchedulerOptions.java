package io.ddupg.garlic.rpc.server;

public class SchedulerOptions {

  private int queueNums = 1;
  private int queueLength = 1024;
  private int handlersPerQueue = 2;

  public int queueNums() {
    return queueNums;
  }

  public SchedulerOptions queueNums(int queueNums) {
    this.queueNums = queueNums;
    return this;
  }

  public int queueLength() {
    return queueLength;
  }

  public SchedulerOptions queueLength(int queueLength) {
    this.queueLength = queueLength;
    return this;
  }

  public int executorPerQueue() {
    return handlersPerQueue;
  }

  public SchedulerOptions executorPerQueue(int executorPerQueue) {
    this.handlersPerQueue = executorPerQueue;
    return this;
  }
}
