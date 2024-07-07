package io.ddupg.garlic.function;

@FunctionalInterface
public interface ExceptionCallable<T, E extends Exception> {
  T call() throws E;
}
