package io.ddupg.garlic.rpc.client;

import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;

public class ConnectionPoolOptions {
  private int limitPerEndpoint = 1;

  public int limitPerEndpoint() {
    return limitPerEndpoint;
  }

  public void limitPerEndpoint(int limitPerEndpoint) {
    Preconditions.checkArgument(limitPerEndpoint > 0);
    this.limitPerEndpoint = limitPerEndpoint;
  }
}
