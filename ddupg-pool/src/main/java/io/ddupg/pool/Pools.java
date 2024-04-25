package io.ddupg.pool;

public final class Pools {

  public static PoolBuilder<Object> createBuilder() {
    return new PoolBuilder<>();
  }
}
