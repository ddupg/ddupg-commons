package io.ddupg.garlic.rpc.client;

import io.ddupg.garlic.function.FunctionUtils;
import io.ddupg.garlic.rpc.Connection;
import io.ddupg.garlic.rpc.Endpoint;
import io.ddupg.garlic.rpc.client.handler.ClientHandler;
import io.ddupg.garlic.rpc.handler.ProtobufDecoder;
import io.ddupg.garlic.rpc.handler.ProtobufEncoder;
import io.ddupg.garlic.rpc.protocol.BaseProtos;
import io.ddupg.garlic.shaded.io.netty.bootstrap.Bootstrap;
import io.ddupg.garlic.shaded.io.netty.channel.Channel;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelFutureListener;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelInitializer;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelOption;
import io.ddupg.garlic.shaded.io.netty.channel.EventLoop;
import io.ddupg.garlic.shaded.io.netty.channel.socket.nio.NioSocketChannel;
import io.ddupg.garlic.shaded.io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.ddupg.garlic.shaded.io.netty.handler.codec.LengthFieldPrepender;

public class ClientConnection implements Connection {

  private final EventLoop eventLoop;
  private final Channel channel;

  public ClientConnection(Endpoint endpoint, EventLoop eventLoop) {
    this.eventLoop = eventLoop;
    this.channel = createChannel(endpoint, eventLoop);
  }

  private Channel createChannel(Endpoint endpoint, EventLoop eventLoop) {
    Bootstrap bootstrap = new Bootstrap();
    return bootstrap.group(eventLoop)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel channel) {
            channel.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                .addLast(new ProtobufDecoder(BaseProtos.Request.getDefaultInstance()))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new ProtobufEncoder())
                .addLast(new ClientHandler(ClientConnection.this))
            ;
          }
        })
        .remoteAddress(endpoint.toAddress())
        .connect()
        .channel();
  }

  @Override
  public boolean isActive() {
    return channel.isActive();
  }

  public void send(Call call) {
    assert eventLoop.inEventLoop();
    channel.writeAndFlush(call).addListener((ChannelFutureListener) future -> {
      assert eventLoop.inEventLoop();

      if (!future.isSuccess()) {
        call.setException(future.cause());
      }
    });
  }

  @Override
  public void shutdown() {
    FunctionUtils.quietly(channel.close()::sync);
  }
}
