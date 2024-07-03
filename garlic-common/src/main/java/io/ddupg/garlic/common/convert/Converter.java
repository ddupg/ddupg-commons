package io.ddupg.garlic.common.convert;

public interface Converter<T, R> {
  R convert(T t);
}
