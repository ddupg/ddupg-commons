package io.ddupg.garlic.rpc.client;

import io.ddupg.garlic.shaded.com.google.protobuf.RpcCallback;
import io.ddupg.garlic.shaded.com.google.protobuf.RpcController;

public class ClientRpcController implements RpcController {


  @Override
  public void reset() {

  }

  @Override
  public boolean failed() {
    return false;
  }

  @Override
  public String errorText() {
    return "";
  }

  @Override
  public void startCancel() {

  }

  @Override
  public void setFailed(String reason) {

  }

  @Override
  public boolean isCanceled() {
    return false;
  }

  @Override
  public void notifyOnCancel(RpcCallback<Object> callback) {
  }
}
