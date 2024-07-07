package io.ddupg.garlic.rpc.server.handler;

import io.ddupg.garlic.rpc.protocol.BaseProtos;
import io.ddupg.garlic.rpc.server.Responses;
import io.ddupg.garlic.rpc.server.Scheduler;
import io.ddupg.garlic.rpc.server.ServerCall;
import io.ddupg.garlic.rpc.server.Services;
import io.ddupg.garlic.shaded.com.google.protobuf.Descriptors;
import io.ddupg.garlic.shaded.com.google.protobuf.Message;
import io.ddupg.garlic.shaded.com.google.protobuf.Service;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelDuplexHandler;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ServerHandler extends ChannelDuplexHandler {

  private final Services services;
  private final Scheduler scheduler;

  public ServerHandler(Services services, Scheduler scheduler) {
    this.services = services;
    this.scheduler = scheduler;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
    if (msg instanceof BaseProtos.Request req) {
      ServerCall call = toCall(req);
      CompletableFuture<BaseProtos.Response> future = scheduler.schedule(call);
      future.whenComplete((r, e) -> {
        if (e != null) {
          r = Responses.failed(call.requestId(), e);
        }
        ctx.channel().writeAndFlush(r);
      });

    } else {
      ctx.fireChannelRead(msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    ctx.channel().close();
  }

  private ServerCall toCall(BaseProtos.Request req) throws IOException {
    BaseProtos.RequestHeader header = req.getHeader();
    Service service = services.get(header.getService());
    Descriptors.MethodDescriptor md = service.getDescriptorForType().findMethodByName(header.getMethod());
    Message msg = service.getRequestPrototype(md).newBuilderForType().mergeFrom(req.getBody().newInput()).build();
    return new ServerCall(header.getRequestId(), service, md, msg);
  }
}
