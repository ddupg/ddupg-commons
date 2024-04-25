package io.ddupg.pool;

public interface Pool<R> {
  R getOrCreate();
}
