package io.ddupg.garlic.pool;

import io.ddupg.garlic.function.FunctionUtil;
import io.ddupg.garlic.pool.listener.ListenerChain;
import io.ddupg.garlic.pool.patrol.Patrolman;
import io.ddupg.garlic.pool.patrol.ResourcesListenerRunner;
import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SharedKeyedPool<K, R extends Resource> implements Iterable<ResourceWrapper<R>> {

  private final KeyedFactory<K, R> factory;
  private final int sizePerK;
  private final Map<KeyWithIdx<K>, ResourceWrapper<R>> resources;

  private final ListenerChain<ResourceWrapper<R>> listeners;

  public SharedKeyedPool(KeyedFactory<K, R> factory, int sizePerKey) {
    this(factory, sizePerKey, ListenerChain.empty());
  }

  public SharedKeyedPool(KeyedFactory<K, R> factory, int sizePerKey, ListenerChain<ResourceWrapper<R>> listeners) {
    this(factory, sizePerKey, listeners, null);
  }

  public SharedKeyedPool(KeyedFactory<K, R> factory, int sizePerKey, ListenerChain<ResourceWrapper<R>> listeners,
                         Patrolman patrolman) {
    Preconditions.checkNotNull(factory);
    Preconditions.checkArgument(sizePerKey > 0);
    Preconditions.checkNotNull(listeners);
    this.factory = factory;
    this.sizePerK = sizePerKey;
    this.listeners = listeners;
    this.resources = Maps.newConcurrentMap();

    if (patrolman != null) {
      patrolman.schedule(TimeUnit.MINUTES.toMillis(1), new ResourcesListenerRunner<>(this, listeners));
    }
  }

  public ResourceWrapper<R> acquire(K k) throws IOException {
    AtomicReference<IOException> ioe = new AtomicReference<>();
    KeyWithIdx<K> key = key(k);
    ResourceWrapper<R> r = resources.computeIfAbsent(key, ignored -> {
      try {
        return create(k);
      } catch (IOException e) {
        ioe.set(e);
      }
      return null;
    });

    if (ioe.get() != null) {
      throw ioe.get();
    }

    onAcquire(r);
    return r;
  }

  public void close() throws IOException {
    for (ResourceWrapper<R> wrapper : resources.values()) {
      FunctionUtil.quietly(() -> onDestroy(wrapper));
    }
    resources.clear();
  }

  private KeyWithIdx<K> key(K k) {
    return PoolUtil.key(k, sizePerK);
  }

  private ResourceWrapper<R> create(K k) throws IOException {
    R r = factory.create(k);
    ResourceWrapper<R> wrapper = new ResourceWrapper<>(r);
    onCreate(wrapper);
    return wrapper;
  }

  private void onCreate(ResourceWrapper<R> r) throws IOException {
    listeners.onCreate(r);
  }

  private void onDestroy(ResourceWrapper<R> r) throws IOException {
    listeners.onDestroy(r);
  }

  private void onAcquire(ResourceWrapper<R> r) throws IOException {
    listeners.onAcquire(r);
  }

  @Override
  public Iterator<ResourceWrapper<R>> iterator() {
    return resources.values().iterator();
  }
}
