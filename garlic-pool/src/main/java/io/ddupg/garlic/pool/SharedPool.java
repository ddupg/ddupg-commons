package io.ddupg.garlic.pool;

import io.ddupg.garlic.function.FunctionUtil;
import io.ddupg.garlic.pool.listener.ListenerChain;
import io.ddupg.garlic.pool.patrol.Patrolman;
import io.ddupg.garlic.pool.patrol.ResourcesListenerRunner;
import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SharedPool<R extends Resource> implements Iterable<ResourceWrapper<R>> {

  private final int size;
  private final ResourceFactory<R> factory;
  private final ResourceWrapper<R>[] resources;
  private int idx;
  private final ListenerChain<ResourceWrapper<R>> listeners;

  public SharedPool(int size, ResourceFactory<R> factory) {
    this(size, factory, ListenerChain.empty());
  }
  public SharedPool(int size, ResourceFactory<R> factory, ListenerChain<ResourceWrapper<R>> listeners) {
    this(size, factory, listeners, null);
  }

  public SharedPool(int size, ResourceFactory<R> factory, ListenerChain<ResourceWrapper<R>> listeners,
                    Patrolman patrolman) {
    Preconditions.checkArgument(size > 0);
    Preconditions.checkNotNull(factory);
    Preconditions.checkNotNull(listeners);
    this.size = size;
    this.factory = factory;
    this.listeners = listeners;
    this.resources = new ResourceWrapper[size];
    idx = 0;
    if (patrolman != null) {
      patrolman.schedule(TimeUnit.MINUTES.toMillis(1), new ResourcesListenerRunner<>(this, listeners));
    }
  }

  public synchronized ResourceWrapper<R> lease() throws IOException {
    ResourceWrapper<R> wrapper;
    if (idx < size) {
      wrapper = create();
      resources[idx] = wrapper;
      idx = (idx + 1) % size;
    } else {
      wrapper = resources[idx];
      if (!wrapper.isValid()) {
        onDestroy(wrapper);
        wrapper = resources[idx] = create();
      }
    }
    idx = (idx + 1) % size;

    onAcquire(wrapper);
    return wrapper;
  }

  public void close() {
    for (ResourceWrapper<R> wrapper : resources) {
      if (wrapper != null) {
        FunctionUtil.quietly(() -> onDestroy(wrapper));
      }
    }
  }

  private ResourceWrapper<R> create() throws IOException {
    R r = factory.create();
    Preconditions.checkState(r.isValid(), "Resource is not valid");
    return new ResourceWrapper<R>(r);
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
    return Arrays.stream(resources).iterator();
  }
}
