package io.ddupg.garlic.common.convert;

public interface BiConverter<T, R> {

  R convert(T t);

  T invert(R r);
}
