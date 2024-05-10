package io.ddupg.garlic.pool;

import io.ddupg.garlic.pool.listener.ListenerChain;
import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultKeyedPool<K, R extends Resource> {

  private final KeyedFactory<K, R> factory;
  private final int maxSizePerK;
  private final ListenerChain<ResourceWrapper<R>> listeners;

  private final Map<K, DefaultPool<R>> pools;

  private final AtomicBoolean closed = new AtomicBoolean(false);

  public DefaultKeyedPool(KeyedFactory<K, R> factory, int maxSizePerK) {
    this(factory, maxSizePerK, ListenerChain.empty());
  }

  public DefaultKeyedPool(KeyedFactory<K, R> factory, int maxSizePerK, ListenerChain<ResourceWrapper<R>> listeners) {
    Preconditions.checkArgument(maxSizePerK > 0);
    Preconditions.checkNotNull(factory);
    Preconditions.checkNotNull(listeners);

    this.factory = factory;
    this.maxSizePerK = maxSizePerK;
    this.listeners = listeners;
    pools = Maps.newConcurrentMap();
  }

  public ResourceWrapper<R> acquire(K k) throws IOException {
    DefaultPool<R> pool = pools.computeIfAbsent(k, this::convertPool);
    return pool.lease();
  }

  private DefaultPool<R> convertPool(K k) {
    return new DefaultPool<>(
        maxSizePerK,
        () -> factory.create(k),
        listeners
    );
  }
}
