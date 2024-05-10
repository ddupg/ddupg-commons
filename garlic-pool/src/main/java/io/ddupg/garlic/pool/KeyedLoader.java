package io.ddupg.garlic.pool;

import java.io.IOException;

public interface KeyedLoader<K, R> {
  R load(K key) throws IOException;
}
