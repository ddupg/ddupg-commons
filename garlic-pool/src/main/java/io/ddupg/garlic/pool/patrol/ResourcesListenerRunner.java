package io.ddupg.garlic.pool.patrol;

import io.ddupg.garlic.pool.listener.ResourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ResourcesListenerRunner<R> implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(ResourcesListenerRunner.class);

  private Iterable<R> iterable;
  private ResourceListener<R> listener;

  public ResourcesListenerRunner(Iterable<R> iterable, ResourceListener<R> listeners) {
    this.iterable = iterable;
    this.listener = listeners;
  }

  @Override
  public void run() {
    iterable.iterator().forEachRemaining(r -> {
      try {
        listener.onPatrol(r);
      } catch (IOException e) {
        LOG.error("Error on patrol", e);
      }
    });
  }
}
