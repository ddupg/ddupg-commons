package io.ddupg.pool;

import java.io.IOException;

public interface ResourceLoader<R> {

  R load() throws IOException;

}
