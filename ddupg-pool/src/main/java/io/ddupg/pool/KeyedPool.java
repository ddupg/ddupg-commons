package io.ddupg.pool;

import java.io.IOException;

public interface KeyedPool<K, R> {
  R getOrCreate(K key, ResourceProvider<R> provider) throws IOException;

  boolean remove(K key, R r);

  void shutdown();
}
