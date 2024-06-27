package io.ddupg.common.convert;

import io.ddupg.common.bytes.Bytes;

public class Converters {

  public static final Converter<Integer, byte[]> INT_BYTES_CONVERTER = Bytes::toBytes;

  public static final Converter<byte[], Integer> BYTES_INT_CONVERTER = Bytes::toInt;

  public static final BiConverter<Integer, byte[]> INT_BYTES_BICONVERTER =
      biConverter(INT_BYTES_CONVERTER, BYTES_INT_CONVERTER);

  public static final BiConverter<byte[], Integer> BYTES_INT_BICONVERTER =
      new ReversedBiConverter<>(INT_BYTES_BICONVERTER);

  public static final Converter<Long, byte[]> LONG_BYTES_CONVERTER = Bytes::toBytes;

  public static final Converter<byte[], Long> BYTES_LONG_CONVERTER = Bytes::toLong;

  public static final BiConverter<Long, byte[]> LONG_BYTES_BICONVERTER =
      biConverter(LONG_BYTES_CONVERTER, BYTES_LONG_CONVERTER);

  public static final BiConverter<byte[], Long> BYTES_LONG_BICONVERTER =
      new ReversedBiConverter<>(LONG_BYTES_BICONVERTER);

  public static <T, R> BiConverter<T, R> biConverter(Converter<T, R> converter, Converter<R, T> inverter) {
    return new BiConverter<T, R>() {
      @Override
      public R convert(T t) {
        return converter.convert(t);
      }

      @Override
      public T invert(R r) {
        return inverter.convert(r);
      }
    };
  }
}
