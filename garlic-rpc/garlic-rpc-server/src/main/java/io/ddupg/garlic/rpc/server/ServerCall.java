package io.ddupg.garlic.rpc.server;

import io.ddupg.garlic.rpc.protocol.BaseProtos;
import io.ddupg.garlic.shaded.com.google.protobuf.Descriptors;
import io.ddupg.garlic.shaded.com.google.protobuf.Message;
import io.ddupg.garlic.shaded.com.google.protobuf.Service;

import java.util.concurrent.CompletableFuture;

public class ServerCall {
  CompletableFuture<BaseProtos.Response> future = new CompletableFuture<>();

  private final long requestId;
  private final Service service;
  private final Descriptors.MethodDescriptor method;

  private Message request;

  public ServerCall(long id, Service service, Descriptors.MethodDescriptor method, Message request) {
    this.requestId = id;
    this.service = service;
    this.method = method;
    this.request = request;
  }

  public long requestId() {
    return requestId;
  }

  public Service service() {
    return service;
  }

  public Descriptors.MethodDescriptor method() {
    return method;
  }

  public Message request() {
    return request;
  }
}
