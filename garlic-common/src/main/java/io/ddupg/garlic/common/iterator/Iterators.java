package io.ddupg.garlic.common.iterator;

import java.util.Iterator;
import java.util.function.Function;

public final class Iterators {
  public static <T, R> Iterable<R> map(Iterable<T> iterable, Function<T, R> fun) {
    return () -> map(iterable.iterator(), fun);
  }

  public static <T, R> Iterator<R> map(Iterator<T> it, Function<T, R> fun) {
    return new Iterator<>() {
      @Override
      public boolean hasNext() {
        return it.hasNext();
      }

      @Override
      public R next() {
        return fun.apply(it.next());
      }
    };
  }
}
