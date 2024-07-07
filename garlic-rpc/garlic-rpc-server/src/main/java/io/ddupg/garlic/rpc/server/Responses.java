package io.ddupg.garlic.rpc.server;

import io.ddupg.garlic.rpc.protocol.BaseProtos.Response;
import io.ddupg.garlic.rpc.protocol.BaseProtos.ResponseException;
import io.ddupg.garlic.rpc.protocol.BaseProtos.ResponseHeader;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Responses {


  public static Response failed(long requestId, Throwable t) {
    ResponseException exception = ResponseException.newBuilder()
        .setExceptionClass(t.getClass().getCanonicalName())
        .setStackTrace(stringifyException(t))
        .build();
    ResponseHeader.Builder header = ResponseHeader.newBuilder()
        .setRequestId(requestId)
        .setException(exception);
    return Response.newBuilder()
        .setHeader(header)
        .build();
  }

  public static String stringifyException(Throwable e) {
    StringWriter stm = new StringWriter();
    PrintWriter wrt = new PrintWriter(stm);
    e.printStackTrace(wrt);
    wrt.close();
    return stm.toString();
  }
}
