package io.ddupg.garlic.rpc.client;

import io.ddupg.garlic.rpc.Endpoint;

public interface ConnectionFactory {
  ClientConnection create(Endpoint endpoint);
}
