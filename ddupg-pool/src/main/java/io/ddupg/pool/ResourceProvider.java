package io.ddupg.pool;

import java.io.IOException;

public interface ResourceProvider<R> {
  R get() throws IOException;
}
