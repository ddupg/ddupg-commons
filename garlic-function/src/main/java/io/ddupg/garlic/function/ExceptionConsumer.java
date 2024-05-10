package io.ddupg.garlic.function;

public interface ExceptionConsumer<T, E extends Exception> {
  public void accept(T t) throws E;
}
