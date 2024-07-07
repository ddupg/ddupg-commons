package io.ddupg.garlic.function;

public class FunctionUtils {

  public static void quietly(ExceptionRunnable<Exception> runnable) {
    try {
      runnable.run();
    } catch (Exception e) {
      // quite
    }
  }
}
