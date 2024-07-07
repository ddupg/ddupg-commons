package io.ddupg.garlic.rpc;

public interface Connection {

  boolean isActive();

  void shutdown();

}
