package io.ddupg.garlic.pool;

import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;

import java.util.concurrent.ThreadLocalRandom;

public class PoolUtil {

  public static <K> KeyWithIdx<K> key(K k, int max) {
    Preconditions.checkArgument(max > 0, "max must be positive.");
    if (max == 1) {
      return KeyWithIdx.of(k, 0);
    }
    ThreadLocalRandom random = ThreadLocalRandom.current();
    return KeyWithIdx.of(k, random.nextInt(max));
  }

}
