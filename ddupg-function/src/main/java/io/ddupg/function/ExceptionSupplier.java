package io.ddupg.function;

@FunctionalInterface
public interface ExceptionSupplier<T> {
  T get() throws Exception;
}
