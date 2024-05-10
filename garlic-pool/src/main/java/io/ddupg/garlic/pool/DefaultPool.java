package io.ddupg.garlic.pool;

import io.ddupg.garlic.function.FunctionUtil;
import io.ddupg.garlic.pool.listener.ListenerChain;
import io.ddupg.garlic.pool.patrol.ResourcesListenerRunner;
import io.ddupg.garlic.pool.patrol.Patrolman;
import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.common.collect.Queues;

import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultPool<R extends Resource> implements Iterable<ResourceWrapper<R>> {

  private final int maxSize;
  private final ResourceFactory<R> factory;
  private final Queue<ResourceWrapper<R>> resources;

  private ListenerChain<ResourceWrapper<R>> listeners;

  private final AtomicBoolean closed;

  public DefaultPool(int maxSize, ResourceFactory<R> factory) {
    this(maxSize, factory, ListenerChain.empty(), null);
  }

  public DefaultPool(int maxSize, ResourceFactory<R> factory, ListenerChain<ResourceWrapper<R>> listeners) {
    this(maxSize, factory, listeners, null);
  }

  public DefaultPool(int maxSize, ResourceFactory<R> factory, ListenerChain<ResourceWrapper<R>> listeners,
                     Patrolman patrolman) {
    Preconditions.checkArgument(maxSize > 0);
    Preconditions.checkNotNull(factory);
    Preconditions.checkNotNull(listeners);
    this.maxSize = maxSize;
    this.factory = factory;
    this.listeners = listeners;
    this.resources = Queues.newArrayDeque();
    this.closed = new AtomicBoolean(false);

    if (patrolman != null) {
      patrolman.schedule(TimeUnit.MINUTES.toMillis(1), new ResourcesListenerRunner<>(this, listeners));
    }
  }

  public synchronized ResourceWrapper<R> lease() throws IOException {
    checkState();

    ResourceWrapper<R> wrapper = resources.poll();
    if (wrapper != null) {
      if (!wrapper.isValid()) {
        return wrapper;
      } else {
        onDestroy(wrapper);
      }
    }
    wrapper = create();
    onAcquire(wrapper);
    return wrapper;
  }

  @Override
  public Iterator<ResourceWrapper<R>> iterator() {
    return resources.iterator();
  }

  public synchronized void close() {
    if (closed.compareAndSet(false, true)) {
      resources.stream()
          .forEach(wrapper -> FunctionUtil.quietly(() -> this.onDestroy(wrapper)));
      resources.clear();

      listeners.close();
    }
  }

  private void checkState() {
    Preconditions.checkState(!closed.get(), "Pool is closed");
  }

  private synchronized boolean release(ResourceWrapper<R> r) {
    checkState();

    if (resources.size() >= maxSize) {
      return false;
    } else {
      resources.add(r);
      return true;
    }
  }

  private ResourceWrapper<R> create() throws IOException {
    R r = factory.create();
    ResourceWrapper<R> wrapper = new ResourceWrapper<R>(r) {
      @Override
      public void release() {
        DefaultPool.this.release(this);
      }
    };

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
}
