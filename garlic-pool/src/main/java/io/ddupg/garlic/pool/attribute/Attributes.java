package io.ddupg.garlic.pool.attribute;

import io.ddupg.garlic.shaded.com.google.common.base.Preconditions;
import io.ddupg.garlic.shaded.com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Attributes {
  private List<Attribute> attributes = Lists.newArrayList();

  public Attributes add(Attribute attribute) {
    Preconditions.checkNotNull(attribute);
    attributes.add(attribute);
    return this;
  }

  /**
   * Find the attribute inherited from the specify class.
   * @param clazz class
   * @return the first attribute inherited from the specify class if existed.
   */
  public <T extends Attribute> Optional<T> find(Class<T> clazz) {
    for (Attribute attribute : attributes) {
      if (attribute.getClass().isAssignableFrom(clazz)) {
        return Optional.of((T) attribute);
      }
    }
    return Optional.empty();
  }

  /**
   * Find the attribute for the given Predicate to become true.
   * @param predicate predicate
   * @return the first attribute inherited from the specify class if existed.
   */
  public Optional<Attribute> find(Predicate<Attribute> predicate) {
    for (Attribute attribute : attributes) {
      if (predicate.test(attribute)) {
        return Optional.of(attribute);
      }
    }
    return Optional.empty();
  }
}
