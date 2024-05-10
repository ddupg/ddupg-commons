package io.ddupg.common.refcnt;

import io.ddupg.shaded.com.google.common.base.Preconditions;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class RefCnt {

  private final AtomicInteger ref = new AtomicInteger(0);

  public int cnt() {
    return ref.get();
  }

  public void retain() {
    ref.incrementAndGet();
  }

  public void countdown() {
    Preconditions.checkArgument(ref.get() > 0, "ref must be positive");
    if (ref.decrementAndGet() == 0) {
      release();
    }
  }

  protected abstract void release();

}
