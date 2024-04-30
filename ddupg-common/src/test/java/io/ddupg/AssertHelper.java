package io.ddupg;

import org.junit.Assert;

import java.util.Iterator;

public final class AssertHelper {

  public static <E> void assertEquals(Iterator<E> expected, Iterator<E> actual) {
    if (equalsRegardingNull(expected, actual)) {
      return;
    }

    if (expected == null) {
      Assert.fail("expected array was null");
    }
    if (actual == null) {
      Assert.fail("actual array was null");
    }

    while (expected.hasNext()) {
      E next = expected.next();
      Assert.assertEquals(next, actual.next());
    }
    Assert.assertFalse(actual.hasNext());
  }

  /**
   * Copied from org.junit.Assert
   */
  private static boolean equalsRegardingNull(Object expected, Object actual) {
    if (expected == null) {
      return actual == null;
    }

    return isEquals(expected, actual);
  }

  private static boolean isEquals(Object expected, Object actual) {
    return expected.equals(actual);
  }
}
