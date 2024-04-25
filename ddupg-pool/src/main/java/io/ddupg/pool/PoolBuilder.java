package io.ddupg.pool;

import io.ddupg.shaded.com.google.common.collect.Lists;

import java.util.List;

public class PoolBuilder<R> {

  private int maxSize;
  private ResourceProvider<R> provider;
  private List<PoolListener> listeners = Lists.newArrayList();

  public PoolBuilder<R> maxSize(int maxSize) {
    this.maxSize = maxSize;
    return this;
  }

  public PoolBuilder<R> resourceProvider(ResourceProvider<R> provider) {
    this.provider = provider;
    return this;
  }

  public PoolBuilder<R> addListener(PoolListener listener) {
    listeners.add(listener);
    return this;
  }
}
