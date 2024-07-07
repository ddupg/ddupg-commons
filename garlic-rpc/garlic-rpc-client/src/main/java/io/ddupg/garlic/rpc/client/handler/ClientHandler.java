package io.ddupg.garlic.rpc.client.handler;

import io.ddupg.garlic.rpc.client.Call;
import io.ddupg.garlic.rpc.client.ClientConnection;
import io.ddupg.garlic.rpc.protocol.BaseProtos;
import io.ddupg.garlic.rpc.protocol.BaseProtos.Response;
import io.ddupg.garlic.shaded.com.google.common.collect.Maps;
import io.ddupg.garlic.shaded.com.google.protobuf.ByteString;
import io.ddupg.garlic.shaded.com.google.protobuf.Message;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelDuplexHandler;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelHandlerContext;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelPromise;

import java.io.IOException;
import java.util.Map;

public class ClientHandler extends ChannelDuplexHandler {

  private final ClientConnection conn;

  private final Map<Long, Call> calls = Maps.newHashMap();

  public ClientHandler(ClientConnection conn) {
    this.conn = conn;
  }

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    if (msg instanceof Call call) {
      calls.put(call.id(), call);
      ctx.write(toRequest(call), promise);
    } else {
      ctx.write(msg, promise);
    }
  }

  private BaseProtos.Request toRequest(Call call) {
    return BaseProtos.Request.newBuilder()
        .mergeHeader(BaseProtos.RequestHeader.newBuilder()
            .setRequestId(call.id())
            .setService(call.service().getFullName())
            .setMethod(call.method().getFullName())
            .build())
        .setBody(ByteString.copyFrom(call.request().toByteArray()))
        .build();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof Response response) {
      Call call = calls.remove(response.getHeader().getRequestId());
      if (call != null) {
        Message body = call.responseType().newBuilderForType().mergeFrom(response.getBody().toByteArray()).build();
        call.setResponse(body);
      }
    } else {
      super.channelRead(ctx, msg);
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    cleanupCalls(new IOException("Connection closed"));
    conn.shutdown();
    ctx.fireChannelInactive();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cleanupCalls(toIOE(cause));
    conn.shutdown();
  }

  private void cleanupCalls(IOException cause) {
    calls.values().forEach(call -> call.setException(cause));
    calls.clear();
  }

  private IOException toIOE(Throwable cause) {
    if (cause instanceof IOException) {
      return (IOException) cause;
    } else {
      return new IOException(cause);
    }
  }
}
