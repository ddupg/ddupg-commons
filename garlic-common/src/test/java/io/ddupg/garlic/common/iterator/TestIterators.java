package io.ddupg.garlic.common.iterator;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

public class TestIterators {

  @Test
  public void testMap() {
    List<Integer> list = Lists.newArrayList(0, 1, 2);
    Iterable<String> mapped = Iterators.map(list, i -> Integer.toString(i));
    Iterator<String> it = mapped.iterator();
    for (int i = 0; i < list.size(); i++) {
      Assert.assertEquals(Integer.toString(i), it.next());
    }
    Assert.assertFalse(it.hasNext());
  }
}