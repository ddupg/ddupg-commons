package io.ddupg.garlic.pool;

import java.io.IOException;

public interface ResourceLoader<R> {
  R load() throws IOException;
}
