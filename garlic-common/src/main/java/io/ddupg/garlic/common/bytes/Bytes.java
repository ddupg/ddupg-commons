package io.ddupg.garlic.common.bytes;

import com.google.common.base.Preconditions;

public class Bytes {

  public static final int INT_BYTE_SIZE = Integer.SIZE / Byte.SIZE;
  public static final int LONG_BYTE_SIZE = Long.SIZE / Byte.SIZE;

  /**
   * Big-endian.
   * @param value value
   * @return bytes
   */
  public static byte[] toBytes(int value) {
    byte[] b = new byte[INT_BYTE_SIZE];
    for (int i = INT_BYTE_SIZE - 1; i >= 0; i--) {
      b[i] = (byte) value;
      value >>>= Byte.SIZE;
    }
    return b;
  }

  public static byte[] toBytes(long value) {
    byte[] b = new byte[LONG_BYTE_SIZE];
    for (int i = LONG_BYTE_SIZE - 1; i >= 0; i--) {
      b[i] = (byte) value;
      value >>>= Byte.SIZE;
    }
    return b;
  }

  public static int toInt(byte[] bytes) {
    return toInt(bytes, 0);
  }

  public static int toInt(byte[] bytes, int offset) {
    return toInt(bytes, offset, INT_BYTE_SIZE);
  }

  public static int toInt(byte[] bytes, int offset, int length) {
    Preconditions.checkNotNull(bytes, "bytes is null");
    Preconditions.checkArgument(offset >= 0 && offset <= bytes.length - length,
        "offset >= 0 && offset <= bytes.length - length");
    Preconditions.checkArgument(length <= INT_BYTE_SIZE, "length <= " + INT_BYTE_SIZE);

    int n = 0;
    for (int i = offset, end = offset + length; i < end; i++) {
      n <<= 8;
      n ^= bytes[i] & 0xFF;
    }
    return n;
  }

  public static long toLong(byte[] bytes) {
    return toLong(bytes, 0);
  }

  public static long toLong(byte[] bytes, int offset) {
    return toLong(bytes, offset, LONG_BYTE_SIZE);
  }

  public static long toLong(byte[] bytes, int offset, int length) {
    Preconditions.checkNotNull(bytes, "bytes is null");
    Preconditions.checkArgument(offset >= 0 && offset <= bytes.length - length,
        "offset >= 0 && offset <= bytes.length - length");
    Preconditions.checkArgument(length <= LONG_BYTE_SIZE, "length <= " + LONG_BYTE_SIZE);

    long n = 0;
    for (int i = offset, end = offset + length; i < end; i++) {
      n <<= 8;
      n ^= bytes[i] & 0xFF;
    }
    return n;
  }
}
