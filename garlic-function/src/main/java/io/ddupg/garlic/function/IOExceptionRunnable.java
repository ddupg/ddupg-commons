package io.ddupg.garlic.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionRunnable {
  void run() throws IOException;
}
