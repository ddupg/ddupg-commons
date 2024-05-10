package io.ddupg.pool;

import java.io.IOException;

import java.io.Closeable;

public interface Pool<R> extends Closeable {
  R getOrCreate(ResourceProvider<R> provider) throws IOException;

  boolean remove(R r);
}
