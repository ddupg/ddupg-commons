package io.ddupg.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionCallable<T> {
  T call() throws IOException;
}
