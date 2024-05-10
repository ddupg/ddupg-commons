package io.ddupg.pool;

import java.io.Closeable;

public abstract class PooledEntry<R> implements Closeable {
  private R resource;

  @Override
  public abstract void close();
}
