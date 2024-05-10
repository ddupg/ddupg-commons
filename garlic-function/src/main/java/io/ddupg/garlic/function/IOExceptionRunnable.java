package io.ddupg.garlic.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionRunnable extends ExceptionRunnable<IOException> {
  void run() throws IOException;
}
