package io.ddupg.garlic.function;

@FunctionalInterface
public interface ExceptionCallable<T> {
  T call() throws Exception;
}
