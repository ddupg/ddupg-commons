package io.ddupg.garlic.pool.listener;

import java.io.IOException;

public interface ResourceListener<R> {
  void onCreate(R r) throws IOException;

  void onAcquire(R r) throws IOException;

  void onDestroy(R r) throws IOException;

  void onPatrol(R r) throws IOException;
}
