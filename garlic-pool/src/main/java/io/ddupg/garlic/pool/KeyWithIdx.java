package io.ddupg.garlic.pool;

import com.google.common.base.Objects;

public class KeyWithIdx<K> {
  private final K key;
  private final int idx;

  private KeyWithIdx(K key, int idx) {
    this.key = key;
    this.idx = idx;
  }

  public static <K> KeyWithIdx<K> of(K key, int idx) {
    return new KeyWithIdx<>(key, idx);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof KeyWithIdx<?> that)) {
      return false;
    }
    return idx == that.idx && Objects.equal(key, that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key, idx);
  }
}
