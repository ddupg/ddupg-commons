package io.ddupg.garlic.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionCallable<T> {
  T call() throws IOException;
}
