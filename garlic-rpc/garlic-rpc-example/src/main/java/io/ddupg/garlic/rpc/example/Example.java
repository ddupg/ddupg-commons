package io.ddupg.garlic.rpc.example;

import io.ddupg.garlic.rpc.Endpoint;
import io.ddupg.garlic.rpc.client.Client;
import io.ddupg.garlic.rpc.client.ClientOptions;
import io.ddupg.garlic.rpc.protocol.DemoProtos;
import io.ddupg.garlic.rpc.protocol.DemoProtos.HelloService;
import io.ddupg.garlic.rpc.server.Server;
import io.ddupg.garlic.rpc.server.ServerOptions;

import java.util.concurrent.atomic.AtomicBoolean;

public class Example {

  public static void main(String[] args) throws Exception {
    new Example().run();
  }

  private void run() throws Exception {

    Server server = null;
    Client client = null;
    try {
      server = startServer();
      client = startClient();

      AtomicBoolean done = new AtomicBoolean(false);
      Endpoint serverEndpoint = Endpoint.of("127.0.0.1", 8080);
      HelloService.Stub stub = HelloService.newStub(client.createRpcChannel(serverEndpoint));
      DemoProtos.HelloRequest req = DemoProtos.HelloRequest.newBuilder().setMsg("world").build();
      stub.say(null, req, resp -> {
        String msg = resp.getMsg();
        System.out.println(msg);
        done.set(true);
      });

      while (!done.get()) {
        Thread.sleep(100);
      }

    } finally {
      if (client != null) {
        client.close();
      }
      if (server != null) {
        server.stop();
      }
    }
  }

  private Server startServer() throws Exception {
    ServerOptions serverOptions = new ServerOptions(8080);
    Server server = new Server(serverOptions);
    server.start();
    return server;
  }

  private Client startClient() throws Exception {
    ClientOptions options = new ClientOptions();
    Client client = new Client(options);
    Endpoint serverEndpoint = Endpoint.of("127.0.0.1", 8080);
    return client;
  }

}
