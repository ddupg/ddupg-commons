package io.ddupg.garlic.pool.attribute;

import java.util.concurrent.atomic.AtomicLong;

public class AccessTs implements Attribute {
  private AtomicLong accessTs;

  public AccessTs() {
    this(System.currentTimeMillis());
  }

  public void accessAt() {
    accessAt(System.currentTimeMillis());
  }

  public AccessTs(long accessTs) {
    this.accessTs = new AtomicLong(accessTs);
  }

  public long accessTs() {
    return accessTs.get();
  }

  public void accessAt(long accessTs) {
    while (true) {
      long last = this.accessTs.get();
      if (last > accessTs) {
        break;
      }
      if (this.accessTs.compareAndSet(last, accessTs)) {
        break;
      }
    }
  }
}
