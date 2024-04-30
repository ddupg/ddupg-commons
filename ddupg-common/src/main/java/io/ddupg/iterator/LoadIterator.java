package io.ddupg.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class LoadIterator<E> implements Iterator<E> {
  private E element;
  private boolean noMore = false;
  @Override
  public boolean hasNext() {
    return !noMore && advance() != null;
  }

  @Override
  public E next() {
    if (hasNext()) {
      E next = advance();
      element = null;
      return next;
    } else {
      throw new NoSuchElementException("no more element");
    }
  }

  private E advance() {
    if (element != null) {
      return element;
    }

    element = load();
    if (element == null) {
      noMore = true;
    }

    return element;
  }

  abstract protected E load();
}
