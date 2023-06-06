package org.csc.java.spring2023.immutable;

import java.util.function.BiFunction;

/**
 * <p>
 * Данный класс отвечает случаю, когда список пуст. Пустой список используется как база для
 * конструирования других списков совместно с классом {@link Cons}.
 * <p>
 * Этот класс намеренно обозначен как package-private, чтобы его нельзя было использовать в других
 * пакетах.
 * <p>
 * Единственное место, в котором он может быть сконструирован - это метод
 * {@link FListUtils#empty()}. Конструирование в других местах будет считаться ошибкой.
 */
final class Nil<T> implements FList<T> {

  @Override
  public int size() {
    return 0;
  }

  @Override
  public T get(int index) {
    throw new ArrayIndexOutOfBoundsException("Empty list don't have index" + index);
  }

  @Override
  public <R> R foldr(R zero, BiFunction<? super T, R, ? extends R> folder) {
    return zero;
  }

  @Override
  public <R> R foldl(R zero, BiFunction<R, ? super T, ? extends R> folder) {
    return zero;
  }

  @Override
  public String toString() {
    return "FList []";
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Nil;
  }

  @Override
  public int hashCode() {
    return 1;
  }

}
