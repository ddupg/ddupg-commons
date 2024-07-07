package io.ddupg.garlic.rpc.server;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import io.ddupg.garlic.rpc.LifeCycle;
import io.ddupg.garlic.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

public class QueueExecutor implements LifeCycle {

  private final int queueLength;
  private final int handlers;

  private RingBuffer<Event> buffer;

  public QueueExecutor(int queueLength, int handlers) {
    this.queueLength = queueLength;
    this.handlers = handlers;
  }

  @Override
  public void start() throws IOException {
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setDaemon(true)
        .setNameFormat("server-rpc-handler-%d")
        .build();

    Disruptor<Event> disruptor = new Disruptor<>(Event::new, queueLength, threadFactory);
    disruptor.handleEventsWith(new Handler());

    disruptor.start();

    buffer = disruptor.getRingBuffer();
  }

  @Override
  public void stop() throws IOException {
  }

  public void enqueue(ServerCall call) {
    long sequence = buffer.next();
    try {
      Event event = buffer.get(sequence);
      event.call = call;
    } finally {
      buffer.publish(sequence);
    }
  }

  private class Event {
    private ServerCall call;
  }

  private class Handler implements EventHandler {

    @Override
    public void onEvent(Object event, long sequence, boolean endOfBatch) throws Exception {

    }
  }
}
