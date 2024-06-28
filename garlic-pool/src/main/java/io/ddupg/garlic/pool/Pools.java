package io.ddupg.garlic.pool;

public final class Pools {

  public static PoolBuilder<Object> createBuilder() {
    return new PoolBuilder<>();
  }
}
