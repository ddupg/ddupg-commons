package io.ddupg.common;

import io.ddupg.shaded.com.google.common.base.Preconditions;
import org.junit.Assert;

public class Waiter {

  private static final long DEFAULT_INTERVAL = 100L; // 100 ms

  private final long timeout;
  private final long interval;
  private final boolean failIfTimeout;
  private final boolean failIfException;

  private Waiter(long timeout, long interval, boolean failIfTimeout, boolean failIfException) {
    Preconditions.checkArgument(timeout > 0, "timeout must be positive.");
    Preconditions.checkArgument(interval > 0, "timeout must be positive.");
    Preconditions.checkArgument(timeout >= interval, "timeout must be >= interval.");
    this.timeout = timeout;
    this.interval = interval;
    this.failIfTimeout = failIfTimeout;
    this.failIfException = failIfException;
  }

  public static <E extends Exception> void waitFor(Predicate<E> predicate, long timeout) {
    waitFor(predicate, timeout, DEFAULT_INTERVAL, true, true);
  }

  public static <E extends Exception> void waitFor(Predicate<E> predicate, long timeout, long interval) {
    waitFor(predicate, timeout, interval, true, true);
  }

  /**
   * Waits up to the specified timeout for the given Waiter.Predicate to become true.
   * @param predicate the predicate to evaluate.
   * @param timeout the timeout in milliseconds to wait for the predicate.
   * @param interval the interval in milliseconds to evaluate predicate.
   * @param failIfTimeout indicates if should fail when times out.
   * @param failIfException indicates if should fail when throwing exceptions.
   * @param <E> thrown if the predicate evaluation could not evaluate.
   */
  public static <E extends Exception> void waitFor(Predicate<E> predicate, long timeout, long interval,
                                                   boolean failIfTimeout, boolean failIfException) {
    Waiter waiter = new Waiter(timeout, interval, failIfTimeout, failIfException);
    waiter.run(predicate);
  }

  private <E extends Exception> void run(Predicate<E> predicate) {
    long startTs = System.currentTimeMillis();
    while (true) {
      try {
        if (predicate.test()) {
          break;
        }
      } catch (Exception e) {
        if (failIfException) {
          throw new AssertionError(e);
        }
      }

      long remainder = startTs + timeout - System.currentTimeMillis();
      if (remainder < 0) {
        if (failIfTimeout) {
          Assert.fail(String.format("Waiting timed out after %s ms", timeout));
        }
        break;
      }

      try {
        Thread.sleep(Math.min(remainder, interval));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    }
  }

  public interface Predicate<E extends Exception> {
    /**
     * Perform a predicate evaluation.
     * @return the boolean result of the evaluation.
     * @throws E thrown if the predicate evaluation could not evaluate.
     */
    boolean test() throws E;
  }
}
