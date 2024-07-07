package io.ddupg.garlic.rpc.handler;

import io.ddupg.garlic.shaded.com.google.protobuf.MessageLite;
import io.ddupg.garlic.shaded.io.netty.buffer.Unpooled;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelHandler;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelHandlerContext;
import io.ddupg.garlic.shaded.io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

@ChannelHandler.Sharable
public class ProtobufEncoder extends MessageToMessageEncoder<MessageLite> {

  protected void encode(ChannelHandlerContext ctx, MessageLite msg, List<Object> out) throws Exception {
    out.add(Unpooled.wrappedBuffer(msg.toByteArray()));
  }
}
