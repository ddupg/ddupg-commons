package io.ddupg.pool;

import java.io.IOException;

public class RoundRobinExclusivePool<R> implements ExclusivePool<R> {

  private final PoolOptions options;
  private final ResourceLoader<R> loader;
  private

  RoundRobinExclusivePool(PoolOptions options, ResourceLoader<R> loader) {
    this.options = options;
    this.loader = loader;
  }

  @Override
  public PooledEntry<R> borrow() {
    return null;
  }

  @Override
  public R getOrCreate(ResourceProvider<R> provider) throws IOException {
    return null;
  }

  @Override
  public boolean remove(R r) {
    return false;
  }

  @Override
  public void close() throws IOException {
  }
}
