package io.ddupg.garlic.pool.listener;

import io.ddupg.garlic.pool.Resource;
import io.ddupg.garlic.pool.ResourceWrapper;
import io.ddupg.garlic.pool.attribute.AccessTs;

import java.util.Optional;

public class ExpireResourceListener<R extends Resource> implements ResourceListenerAdapter<ResourceWrapper<R>> {

  private final long idleTime;

  public ExpireResourceListener(long idleTime) {
    this.idleTime = idleTime;
  }

  @Override
  public void onAcquire(ResourceWrapper<R> wrapper) {
    find(wrapper).ifPresent(AccessTs::accessAt);
  }

  @Override
  public void onPatrol(ResourceWrapper<R> wrapper) {
    find(wrapper).ifPresent(accessTs -> {
      if (accessTs.accessTs() < System.currentTimeMillis() - idleTime) {
        wrapper.expired(true);
      }
    });
  }

  private Optional<AccessTs> find(ResourceWrapper<R> wrapper) {
    return wrapper.attributes().find(AccessTs.class);
  }
}
