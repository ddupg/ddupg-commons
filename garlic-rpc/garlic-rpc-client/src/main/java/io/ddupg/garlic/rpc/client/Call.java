package io.ddupg.garlic.rpc.client;

import io.ddupg.garlic.shaded.com.google.protobuf.Descriptors.MethodDescriptor;
import io.ddupg.garlic.shaded.com.google.protobuf.Descriptors.ServiceDescriptor;
import io.ddupg.garlic.shaded.com.google.protobuf.Message;

import java.util.concurrent.CompletableFuture;

public class Call {
  private final long id;
  private final ServiceDescriptor service;
  private final MethodDescriptor method;
  private final Message request;
  private final Message responseType;
  private Message response;

  private int timeout;
  private int retries;

  private final CompletableFuture<Void> future;

  public Call(long id, MethodDescriptor method, Message request, Message responseType) {
    this.id = id;
    this.service = method.getService();
    this.method = method;
    this.request = request;
    this.responseType = responseType;
    future = new CompletableFuture<>();
  }

  public void setException(Throwable e) {
    future.completeExceptionally(e);
  }

  public void setResponse(Message response) {
    this.response = response;
    future.complete(null);
  }

  public long id() {
    return id;
  }

  public ServiceDescriptor service() {
    return service;
  }

  public MethodDescriptor method() {
    return method;
  }

  public Message request() {
    return request;
  }

  public Message responseType() {
    return responseType;
  }

  public Message response() {
    return response;
  }

  public int timeout() {
    return timeout;
  }

  public void timeout(int timeout) {
    this.timeout = timeout;
  }

  public int retries() {
    return retries;
  }

  public void retries(int retries) {
    this.retries = retries;
  }

  public CompletableFuture<Void> future() {
    return future;
  }
}
