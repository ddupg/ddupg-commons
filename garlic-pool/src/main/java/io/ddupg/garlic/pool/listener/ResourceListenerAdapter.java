package io.ddupg.garlic.pool.listener;

import java.io.IOException;

public interface ResourceListenerAdapter<R> extends ResourceListener<R> {

  @Override
  default void onCreate(R r) throws IOException {
    // NOOP
  }

  @Override
  default void onAcquire(R r) throws IOException {
    // NOOP
  }

  @Override
  default void onDestroy(R r) throws IOException {
    // NOOP
  }

  @Override
  default void onPatrol(R r) throws IOException {
    // NOOP
  }
}
