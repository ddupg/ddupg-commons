package io.ddupg.garlic.rpc.client;

import io.ddupg.garlic.function.FunctionUtils;
import io.ddupg.garlic.rpc.Endpoint;
import io.ddupg.garlic.shaded.com.google.protobuf.Descriptors;
import io.ddupg.garlic.shaded.com.google.protobuf.Message;
import io.ddupg.garlic.shaded.com.google.protobuf.RpcCallback;
import io.ddupg.garlic.shaded.com.google.protobuf.RpcChannel;
import io.ddupg.garlic.shaded.com.google.protobuf.RpcController;
import io.ddupg.garlic.shaded.io.netty.channel.EventLoopGroup;
import io.ddupg.garlic.shaded.io.netty.channel.nio.NioEventLoopGroup;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicLong;

public class Client implements Closeable {

  private final ClientOptions options;
  private final EventLoopGroup eventLoopGroup;

  private final AtomicLong requestId = new AtomicLong(0);
  private final ConnectionPool pool;

  public Client(ClientOptions options) {
    this.options = options;
    eventLoopGroup = new NioEventLoopGroup(options.nettyEventLoopGroupThreads());
    pool = new ConnectionPool(options.connectionPoolOptions(), eventLoopGroup);
  }

  private long nextRequestId() {
    return requestId.incrementAndGet();
  }

  public RpcChannel createRpcChannel(Endpoint endpoint) {
    return new RpcChannel() {
      @Override
      public void callMethod(Descriptors.MethodDescriptor method, RpcController controller,
                             Message request, Message responsePrototype, RpcCallback<Message> done) {
        Call call = new Call(nextRequestId(), method, request, responsePrototype);

        // todo: handle exceptions
        call.future().whenComplete((r, e) -> {
          if (e != null) {
            controller.setFailed(e.getMessage());
          } else {
            done.run(call.response());
          }
        });

        pool.acquire(endpoint)
            .thenAccept(conn -> conn.send(call));
      }
    };
  }

  @Override
  public void close() {
    FunctionUtils.quietly(eventLoopGroup.shutdownGracefully()::sync);
    FunctionUtils.quietly(pool::close);
  }
}
