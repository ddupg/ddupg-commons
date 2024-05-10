package io.ddupg.garlic.function;

public class FunctionUtil {
  public static void quietly(ExceptionRunnable<Exception> runnable) {
    try {
      runnable.run();
    } catch (Exception e) {
      // be quietly.
    }
  }
}
