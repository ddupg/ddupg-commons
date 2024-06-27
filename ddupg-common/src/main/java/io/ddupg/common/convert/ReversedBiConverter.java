package io.ddupg.common.convert;

public class ReversedBiConverter<T, R> implements BiConverter<T, R> {

  private final BiConverter<R, T> delegate;

  public ReversedBiConverter(BiConverter<R, T> delegate) {
    this.delegate = delegate;
  }

  @Override
  public R convert(T t) {
    return delegate.invert(t);
  }

  @Override
  public T invert(R r) {
    return delegate.convert(r);
  }
}
