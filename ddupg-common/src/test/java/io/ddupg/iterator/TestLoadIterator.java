package io.ddupg.iterator;

import io.ddupg.AssertHelper;
import io.ddupg.shaded.com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

public class TestLoadIterator {

  @Test
  public void test() {
    AssertHelper.assertEquals(Lists.<Integer>newArrayList().iterator(), new MyIterator(new Integer[0]));

    List<Integer> list = Lists.newArrayList(1, 2, 3);
    AssertHelper.assertEquals(list.iterator(), new MyIterator(list.toArray(new Integer[0])));
  }

  @Test
  public void testException() {
    List<Integer> list = Lists.newArrayList(1, 2, 3);
    MyIterator it = new MyIterator(list.toArray(new Integer[0]));
    for (int i = 0; i < list.size(); i++) {
      it.next();
    }
    Assertions
        .assertThatThrownBy(it::next)
        .isInstanceOf(NoSuchElementException.class);
  }

  static class MyIterator extends LoadIterator<Integer> {

    private final Integer[] elements;
    private int idx;

    public MyIterator(Integer[] elements) {
      this.elements = elements;
      idx = 0;
    }

    @Override
    protected Integer load() {
      return elements != null && idx < elements.length ? elements[idx ++] : null;
    }
  }
}