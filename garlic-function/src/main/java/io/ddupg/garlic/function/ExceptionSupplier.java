package io.ddupg.garlic.function;

@FunctionalInterface
public interface ExceptionSupplier<T> {
  T get() throws Exception;
}
