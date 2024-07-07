package io.ddupg.garlic.rpc;

import java.net.InetSocketAddress;

public class Endpoint {

  private final String host;
  private final int port;

  public static Endpoint of(String host, int port) {
    return new Endpoint(host, port);
  }

  private Endpoint(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public InetSocketAddress toAddress() {
    return new InetSocketAddress(host, port);
  }

  public String host() {
    return host;
  }

  public int port() {
    return port;
  }
}
