package io.ddupg.pool;

public interface ExclusivePool<R> extends Pool<R> {

  PooledEntry<R> borrow();

}
