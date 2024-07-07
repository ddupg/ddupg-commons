package io.ddupg.garlic.rpc.handler;

import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.protobuf.MessageLite;
import io.ddupg.garlic.shaded.io.netty.buffer.ByteBuf;
import io.ddupg.garlic.shaded.io.netty.buffer.ByteBufUtil;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelHandler;
import io.ddupg.garlic.shaded.io.netty.channel.ChannelHandlerContext;
import io.ddupg.garlic.shaded.io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@ChannelHandler.Sharable
public class ProtobufDecoder extends MessageToMessageDecoder<ByteBuf> {

  private final MessageLite prototype;

  public ProtobufDecoder(MessageLite prototype) {
    this.prototype = Preconditions.checkNotNull(prototype);
  }

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> out) throws Exception {
    final byte[] array;
    final int offset;
    final int length = msg.readableBytes();
    if (msg.hasArray()) {
      array = msg.array();
      offset = msg.arrayOffset() + msg.readerIndex();
    } else {
      array = ByteBufUtil.getBytes(msg, msg.readerIndex(), length, false);
      offset = 0;
    }

    out.add(prototype.newBuilderForType().mergeFrom(array, offset, length).build());
  }
}
