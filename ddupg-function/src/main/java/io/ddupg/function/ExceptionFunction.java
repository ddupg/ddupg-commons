package io.ddupg.function;

@FunctionalInterface
public interface ExceptionFunction<T, R> {
  R apply(T t) throws Exception;
}
