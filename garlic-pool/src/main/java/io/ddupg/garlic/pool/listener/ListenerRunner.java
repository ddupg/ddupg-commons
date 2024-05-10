package io.ddupg.garlic.pool.listener;

import io.ddupg.garlic.pool.patrol.ResourcesListenerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ListenerRunner<R> implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(ResourcesListenerRunner.class);

  private R r;
  private ResourceListener<R> listener;

  public ListenerRunner(R r, ResourceListener<R> listeners) {
    this.r = r;
    this.listener = listeners;
  }

  @Override
  public void run() {
    try {
      listener.onPatrol(r);
    } catch (IOException e) {
      LOG.error("Error on patrol", e);
    }
  }
}
