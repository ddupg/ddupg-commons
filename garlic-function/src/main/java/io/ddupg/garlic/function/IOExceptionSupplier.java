package io.ddupg.garlic.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionSupplier<T> extends ExceptionSupplier<T, IOException> {
  T get() throws IOException;
}
