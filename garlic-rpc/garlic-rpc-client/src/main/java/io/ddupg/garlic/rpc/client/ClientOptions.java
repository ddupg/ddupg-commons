package io.ddupg.garlic.rpc.client;

public class ClientOptions {
  private int timeout = 5000;
  private int retries = 3;
  private int threads = 1;
  private int nettyEventLoopGroupThreads = 0;

  private ConnectionPoolOptions connectionPoolOptions = new ConnectionPoolOptions();

  public int timeout() {
    return timeout;
  }

  public int retries() {
    return retries;
  }

  public int threads() {
    return threads;
  }

  public int nettyEventLoopGroupThreads() {
    return nettyEventLoopGroupThreads;
  }

  public ConnectionPoolOptions connectionPoolOptions() {
    return connectionPoolOptions;
  }
}
