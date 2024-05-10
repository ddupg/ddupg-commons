package io.ddupg.garlic.pool;

import java.io.IOException;

public interface ResourceFactory<R> {
  R create() throws IOException;
}
