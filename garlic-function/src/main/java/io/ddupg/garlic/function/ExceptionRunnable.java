package io.ddupg.garlic.function;

@FunctionalInterface
public interface ExceptionRunnable {
  void run() throws Exception;
}
