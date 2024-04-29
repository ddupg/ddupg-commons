package io.ddupg.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionSupplier<T> {
  T get() throws IOException;
}
