package io.ddupg.garlic.pool;

import io.ddupg.garlic.pool.attribute.Attribute;
import io.ddupg.garlic.pool.attribute.Attributes;
import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;

public class ResourceWrapper<R extends Resource> {
  private final R resource;
  private boolean expired;
  private Attributes attributes = new Attributes();

  public ResourceWrapper(R resource) {
    Preconditions.checkNotNull(resource);
    this.resource = resource;
    this.expired = false;
  }

  public R resource() {
    return resource;
  }

  public boolean expired() {
    return expired;
  }

  public void expired(boolean expired) {
    this.expired = expired;
  }

  public ResourceWrapper<R> addAttribute(Attribute attribute) {
    this.attributes.add(attribute);
    return this;
  }

  public Attributes attributes() {
    return attributes;
  }

  public void release() {
  }

  public boolean isValid() {
    return expired && resource.isValid();
  }
}
