package io.ddupg.garlic.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionCallable<T> extends ExceptionCallable<T, IOException> {
  T call() throws IOException;
}
