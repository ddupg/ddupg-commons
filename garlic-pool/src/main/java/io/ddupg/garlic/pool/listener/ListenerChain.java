package io.ddupg.garlic.pool.listener;

import io.ddupg.garlic.function.IOExceptionConsumer;
import io.ddupg.garlic.shaded.com.google.common.collect.Iterables;
import io.ddupg.garlic.shaded.com.google.common.collect.Lists;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class ListenerChain<R> implements ResourceListener<R>, Closeable {

  private final List<ResourceListener<R>> listeners = Lists.newCopyOnWriteArrayList();
  private boolean interruptIfError = false;

  public static <R> ListenerChain<R> empty() {
    return new ListenerChain<R>();
  }

  public ListenerChain<R> add(ResourceListener<R> listener) {
    listeners.add(listener);
    return this;
  }

  public ListenerChain<R> addAll(Iterable<ResourceListener<R>> listeners) {
    List<ResourceListener<R>> tmp = Lists.newArrayList();
    Iterables.addAll(tmp, listeners);

    this.listeners.removeAll(tmp);
    return this;
  }

  public ListenerChain<R> remove(ResourceListener<R> listener) {
    listeners.remove(listener);
    return this;
  }

  public ListenerChain<R> removeAll(Iterable<ResourceListener<R>> listeners) {
    List<ResourceListener<R>> tmp = Lists.newArrayList();
    Iterables.addAll(tmp, listeners);

    this.listeners.removeAll(tmp);
    return this;
  }

  public boolean interruptIfError() {
    return interruptIfError;
  }

  public ListenerChain<R> interruptIfError(boolean interruptIfError) {
    this.interruptIfError = interruptIfError;
    return this;
  }

  @Override
  public void onCreate(R r) throws IOException {
    iterate(listener -> listener.onCreate(r));
  }

  @Override
  public void onAcquire(R r) throws IOException {
    iterate(listener -> listener.onAcquire(r));
  }

  @Override
  public void onDestroy(R r) throws IOException {
    iterate(listener -> listener.onDestroy(r));
  }

  @Override
  public void onPatrol(R r) throws IOException {
    iterate(listener -> listener.onPatrol(r));
  }

  private void iterate(IOExceptionConsumer<ResourceListener<R>> consumer) throws IOException {
    IOException execption = null;
    for (ResourceListener<R> listener : listeners) {
      try {
        consumer.accept(listener);
      } catch (IOException e) {
        execption = e;
        if (interruptIfError) {
          throw e;
        }
      }
    }
    if (execption != null) {
      throw execption;
    }
  }

  @Override
  public void close() {
    listeners.clear();
  }
}
