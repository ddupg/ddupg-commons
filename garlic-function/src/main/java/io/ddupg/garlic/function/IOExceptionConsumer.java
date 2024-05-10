package io.ddupg.garlic.function;

import java.io.IOException;

public interface IOExceptionConsumer<T> extends ExceptionConsumer<T, IOException> {
  public void accept(T t) throws IOException;
}
