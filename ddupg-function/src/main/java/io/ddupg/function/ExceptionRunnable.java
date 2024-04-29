package io.ddupg.function;

@FunctionalInterface
public interface ExceptionRunnable {
  void run() throws Exception;
}
