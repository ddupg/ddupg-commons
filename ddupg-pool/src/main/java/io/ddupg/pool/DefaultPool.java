package io.ddupg.pool;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

public class DefaultPool<R> implements Pool<R> {

  private final int maxSize;
  private int idx;
  private final List<R> resources;

  public DefaultPool(int maxSize) {
    this.maxSize = maxSize;
    this.resources = Lists.newArrayListWithCapacity(maxSize);
  }

  @Override
  public R getOrCreate(ResourceProvider<R> provider) throws IOException {
    R r;
    if (resources.size() < maxSize) {
      r = provider.get();
      resources.add(r);
    } else {
      r = resources.get(idx);
      idx = (idx + 1) % maxSize;
    }
    return r;
  }

  @Override
  public boolean remove(R r) {
    return resources.remove(r);
  }

  @Override
  public void close() throws IOException {
  }
}
