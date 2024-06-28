package io.ddupg.garlic.common.bytes;

import io.ddupg.garlic.common.convert.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class TestBytes {

  @Test
  public void testInt() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    for (int i = 0; i < 100; i++) {
      test(Bytes::toBytes, Bytes::toInt, rnd.nextInt());
    }

    test(Bytes::toBytes, Bytes::toInt, 0);
    test(Bytes::toBytes, Bytes::toInt, 1);
    test(Bytes::toBytes, Bytes::toInt, -1);
    test(Bytes::toBytes, Bytes::toInt, Integer.MAX_VALUE);
    test(Bytes::toBytes, Bytes::toInt, Integer.MIN_VALUE);
  }

  @Test
  public void testLong() {
    ThreadLocalRandom rnd = ThreadLocalRandom.current();
    for (int i = 0; i < 100; i++) {
      test(Bytes::toBytes, Bytes::toLong, rnd.nextLong());
    }

    test(Bytes::toBytes, Bytes::toLong, 0L);
    test(Bytes::toBytes, Bytes::toLong, 1L);
    test(Bytes::toBytes, Bytes::toLong, -1L);
    test(Bytes::toBytes, Bytes::toLong, Long.MAX_VALUE);
    test(Bytes::toBytes, Bytes::toLong, Long.MIN_VALUE);
  }

  private <T, R> void test(Converter<T, R> converter, Converter<R, T> inverter, T value) {
    R r = converter.convert(value);
    Assert.assertEquals(value, inverter.convert(r));
  }
}
