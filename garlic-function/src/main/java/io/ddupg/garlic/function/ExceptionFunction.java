package io.ddupg.garlic.function;

@FunctionalInterface
public interface ExceptionFunction<T, R> {
  R apply(T t) throws Exception;
}
