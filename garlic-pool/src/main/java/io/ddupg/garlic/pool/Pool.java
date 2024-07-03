package io.ddupg.garlic.pool;

public interface Pool<R> {
  R getOrCreate();
}
