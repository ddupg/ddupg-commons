package io.ddupg.garlic.common.convert;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class TestConverters {

  @Test
  public void testInt() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    BiConverter<Integer, byte[]> converter = Converters.INT_BYTES_BICONVERTER;
    for (int i = 0; i < 100; i++) {
      test(converter, rnd.nextInt());
    }

    test(converter, 0);
    test(converter, 1);
    test(converter, -1);
    test(converter, Integer.MAX_VALUE);
    test(converter, Integer.MIN_VALUE);
  }

  @Test
  public void testLong() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    BiConverter<Long, byte[]> converter = Converters.LONG_BYTES_BICONVERTER;
    for (int i = 0; i < 100; i++) {
      test(converter, rnd.nextLong());
    }

    test(converter, 0L);
    test(converter, 1L);
    test(converter, -1L);
    test(converter, Long.MAX_VALUE);
    test(converter, Long.MIN_VALUE);
  }

  private <T, R> void test(BiConverter<T, R> converter, T value) {
    R r = converter.convert(value);
    Assert.assertEquals(value, converter.invert(r));
  }
}