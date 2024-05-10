package io.ddupg.pool;

public class PoolOptions {
  private int limit;

  public int limit() {
    return limit;
  }

  public PoolOptions limit(int limit) {
    this.limit = limit;
    return this;
  }
}
