package org.csc.java.spring2023.iterables;

import java.util.Objects;

/**
 * Вспомогательный класс для хранения пар элементов. <br/><br/> Начиная с Java 16 может быть заменён
 * на Record.
 *
 * @param <A> тип первого элемента пары
 * @param <B> тип второго элемента пары
 */
public final class Pair<A, B> {

  private final A first;
  private final B second;

  public Pair(A first, B second) {
    this.first = first;
    this.second = second;
  }

  public static <A, B> Pair<A, B> of(A first, B second) {
    return new Pair<>(first, second);
  }

  public A getFirst() {
    return first;
  }

  public B getSecond() {
    return second;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public String toString() {
    return "Pair{" +
        "first=" + first +
        ", second=" + second +
        '}';
  }
}
