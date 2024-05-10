package io.ddupg.pool;

import io.ddupg.shaded.com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;

public class KeyedSharedPool<K, R> implements KeyedPool<K, R> {

  private Map<K, Pool<R>> pools = Maps.newConcurrentMap();

  @Override
  public R getOrCreate(K key, ResourceProvider<R> provider) throws IOException {
    Pool<R> pool = pools.computeIfAbsent(key, k -> new DefaultPool<>(3));
    return pool.getOrCreate(provider);
  }

  @Override
  public boolean remove(K key, R r) {
    Pool<R> pool = pools.get(key);
    return pool.remove(r);
  }

  @Override
  public void shutdown() {

  }
}
