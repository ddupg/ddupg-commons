package io.ddupg.garlic.function;

@FunctionalInterface
public interface ExceptionRunnable<E extends Exception> {
  void run() throws E;
}
