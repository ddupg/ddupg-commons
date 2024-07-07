package io.ddupg.garlic.rpc.client;

import io.ddupg.garlic.rpc.Endpoint;
import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.common.collect.Maps;
import io.ddupg.garlic.shaded.io.netty.channel.EventLoop;
import io.ddupg.garlic.shaded.io.netty.channel.EventLoopGroup;

import java.io.Closeable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionPool implements Closeable {

  private final ConnectionPoolOptions options;
  private final EventLoopGroup eventLoopGroup;
  private final Map<Key, CompletableFuture<ClientConnection>> pool = Maps.newConcurrentMap();
  private final AtomicBoolean closed =  new AtomicBoolean(false);

  public ConnectionPool(ConnectionPoolOptions options,
                        EventLoopGroup eventLoopGroup) {
    this.options = options;
    this.eventLoopGroup = eventLoopGroup;
  }

  public CompletableFuture<ClientConnection> acquire(final Endpoint endpoint) {
    checkState();

    Key key = newKey(endpoint);

    return acquireOrCreate(key);
  }

  private CompletableFuture<ClientConnection> acquireOrCreate(final Key key) {
    CompletableFuture<ClientConnection> future = pool.computeIfAbsent(key, this::create);

    return future.thenCompose(conn -> {
      if (!conn.isActive()) {
        pool.remove(key, future);
        return pool.computeIfAbsent(key, this::create);
      } else {
        return CompletableFuture.completedFuture(conn);
      }
    });
  }

  private CompletableFuture<ClientConnection> create(final Key key) {
    CompletableFuture<ClientConnection> future = new CompletableFuture<>();

    EventLoop eventLoop = eventLoopGroup.next();
    CompletableFuture.runAsync(() -> {
      ClientConnection conn = new ClientConnection(key.endpoint, eventLoop);
      future.complete(conn);
    }, eventLoop);

    return future;
  }

  private Key newKey(Endpoint endpoint) {
    int idx = ThreadLocalRandom.current().nextInt(options.limitPerEndpoint());
    return new Key(endpoint, idx);
  }

  private void checkState() {
    Preconditions.checkState(!closed.get(), "Connection pool is closed");
  }

  @Override
  public void close() {
    if (closed.compareAndSet(false, true)) {
      pool.values().forEach(future -> {
        if (future.isDone()) {
          future.join().shutdown();
        } else {
          future.whenComplete((conn, e) -> {
            if (e != null) {
              conn.shutdown();
            }
          });
        }
      });
    }
  }

  static class Key {
    private final Endpoint endpoint;
    private final int id;

    static Key of(Endpoint endpoint, int id) {
      return new Key(endpoint, id);
    }

    private Key(Endpoint endpoint, int id) {
      this.endpoint = endpoint;
      this.id = id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Key key)) {
        return false;
      }
      return id == key.id && Objects.equals(endpoint, key.endpoint);
    }

    @Override
    public int hashCode() {
      return Objects.hash(endpoint, id);
    }
  }
}
