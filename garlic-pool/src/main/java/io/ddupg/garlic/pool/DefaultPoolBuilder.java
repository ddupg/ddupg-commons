package io.ddupg.garlic.pool;

import io.ddupg.garlic.pool.listener.ExpireResourceListener;
import io.ddupg.garlic.pool.listener.ListenerChain;
import io.ddupg.garlic.pool.listener.ResourceListener;
import io.ddupg.garlic.pool.patrol.Patrolman;
import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.common.collect.Lists;

import java.util.List;

public class DefaultPoolBuilder<R extends Resource> {

  private int maxSize;
  private ResourceFactory<R> factory;
  private ListenerChain<ResourceWrapper<R>> listeners = new ListenerChain<>();
  private List<Runnable> patrolTasks = Lists.newArrayList();
  private boolean needPatrol = false;

  public DefaultPoolBuilder<R> idleTime(long idleTime) {
    Preconditions.checkArgument(idleTime > 0, "idleTime must be positive");
    addListener(new ExpireResourceListener<>(idleTime));
    return this;
  }

  public DefaultPoolBuilder<R> maxSize(int maxSize) {
    Preconditions.checkArgument(maxSize > 0, "maxSize must be positive");
    this.maxSize = maxSize;
    return this;
  }

  public DefaultPoolBuilder<R> factory(ResourceFactory<R> factory) {
    Preconditions.checkNotNull(factory, "factory cannot be null");
    this.factory = factory;
    return this;
  }

  public DefaultPoolBuilder<R> addListener(ResourceListener<ResourceWrapper<R>> listener) {
    Preconditions.checkNotNull(listener, "listener cannot be null");
    listeners.add(listeners);
    return this;
  }

  public DefaultPool<R> build() {
    Patrolman patrolman = null;
    if (needPatrol) {
      patrolman = new Patrolman();
    }
    DefaultPool<R> pool = new DefaultPool<>(maxSize, factory, listeners, patrolman);
    return pool;
  }
}
