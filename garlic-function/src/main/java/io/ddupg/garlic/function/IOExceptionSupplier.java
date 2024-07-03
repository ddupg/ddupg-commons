package io.ddupg.garlic.function;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionSupplier<T> {
  T get() throws IOException;
}
