package io.ddupg.common.convert;

import io.ddupg.common.bytes.Bytes;

public class Converters {

  public static final Converter<Integer, byte[]> INT_BYTES_CONVERTER = Bytes::toBytes;

  public static final BiConverter<Integer, byte[]> INT_BYTES_BICONVERTER = new BiConverter<Integer, byte[]>() {
    @Override
    public byte[] convert(Integer value) {
      return Bytes.toBytes(value);
    }

    @Override
    public Integer invert(byte[] bytes) {
      return Bytes.toInt(bytes);
    }
  };

  public static final BiConverter<byte[], Integer> BYTES_INT_BICONVERTER =
      new ReversedBiConverter<>(INT_BYTES_BICONVERTER);

  public static final Converter<Long, byte[]> LONG_BYTES_CONVERTER = Bytes::toBytes;

  public static final BiConverter<Long, byte[]> LONG_BYTES_BICONVERTER = new BiConverter<>() {
    @Override
    public byte[] convert(Long value) {
      return Bytes.toBytes(value);
    }

    @Override
    public Long invert(byte[] bytes) {
      return Bytes.toLong(bytes);
    }
  };

  public static final BiConverter<byte[], Long> BYTES_LONG_BICONVERTER =
      new ReversedBiConverter<>(LONG_BYTES_BICONVERTER);

}
