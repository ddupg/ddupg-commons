package io.ddupg.garlic.rpc.server;

import io.ddupg.garlic.function.FunctionUtils;
import io.ddupg.garlic.rpc.LifeCycle;
import io.ddupg.garlic.rpc.handler.ProtobufDecoder;
import io.ddupg.garlic.rpc.handler.ProtobufEncoder;
import io.ddupg.garlic.rpc.protocol.BaseProtos;
import io.ddupg.garlic.rpc.server.handler.ServerHandler;
import io.ddupg.garlic.shaded.io.netty.bootstrap.ServerBootstrap;
import io.ddupg.garlic.shaded.io.netty.channel.Channel;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelInitializer;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelOption;
import io.ddupg.garlic.shaded.io.netty.channel.EventLoopGroup;
import io.ddupg.garlic.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.ddupg.garlic.shaded.io.netty.channel.socket.SocketChannel;
import io.ddupg.garlic.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;
import io.ddupg.garlic.shaded.io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.ddupg.garlic.shaded.io.netty.handler.codec.LengthFieldPrepender;

import java.io.IOException;
import java.io.InterruptedIOException;

public class Server implements LifeCycle {

  private final ServerOptions options;

  private EventLoopGroup boss;
  private EventLoopGroup worker;
  private Channel channel;

  private final Services services;
  private final Scheduler scheduler;

  public Server(ServerOptions options) {
    this.options = options;
    this.services = new Services();
    this.scheduler = new Scheduler(options.schedulerOptions());
  }

  @Override
  public void start() throws IOException {
    ServerBootstrap bootstrap = new ServerBootstrap();
    boss = new NioEventLoopGroup(options.bossThreads());
    worker = new NioEventLoopGroup(options.workerThreads());
    try {
      channel = bootstrap.group(boss, worker)
          .channel(NioServerSocketChannel.class)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childOption(ChannelOption.SO_REUSEADDR, true)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
              channel.pipeline()
                  .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                  .addLast(new ProtobufDecoder(BaseProtos.Response.getDefaultInstance()))
                  .addLast(new LengthFieldPrepender(4))
                  .addLast(new ProtobufEncoder())
                  .addLast(new ServerHandler(services, scheduler));
            }
          })
          .bind(options.port())
          .sync()
          .channel();
    } catch (InterruptedException e) {
      throw new InterruptedIOException(e.getMessage());
    }
  }

  @Override
  public void stop() {
    FunctionUtils.quietly(channel.close()::sync);
    FunctionUtils.quietly(boss.shutdownGracefully()::sync);
    FunctionUtils.quietly(worker.shutdownGracefully()::sync);
  }
}
