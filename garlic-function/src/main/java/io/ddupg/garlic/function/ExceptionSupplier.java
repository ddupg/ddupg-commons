package io.ddupg.garlic.function;

@FunctionalInterface
public interface ExceptionSupplier<T, E extends Exception> {
  T get() throws E;
}
