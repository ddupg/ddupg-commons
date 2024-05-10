package io.ddupg.garlic.pool;

import java.io.IOException;

public interface KeyedFactory<K, R> {
  R create(K key) throws IOException;
}
