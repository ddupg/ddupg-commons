package io.ddupg.common.convert;

public interface Converter<T, R> {
  R convert(T t);
}
