package io.ddupg.function;

@FunctionalInterface
public interface ExceptionCallable<T> {
  T call() throws Exception;
}
