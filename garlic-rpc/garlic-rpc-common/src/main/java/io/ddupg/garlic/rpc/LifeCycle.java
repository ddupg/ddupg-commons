package io.ddupg.garlic.rpc;

import java.io.IOException;

public interface LifeCycle {
  void start() throws IOException;

  void stop() throws IOException;
}
