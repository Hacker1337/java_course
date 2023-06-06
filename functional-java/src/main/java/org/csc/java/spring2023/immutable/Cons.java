package org.csc.java.spring2023.immutable;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiFunction;

/**
 * Данный класс должен позволять создавать создавать новый список, объединяя вместе новый элемент и
 * какой-то уже существующий список. Новый элемент является головой списка, а существующий список -
 * хвостом (head & tail).
 * <p>
 * Этот класс намеренно обозначен как package-private, чтобы его нельзя было использовать в других
 * пакетах.
 * <p>
 * Единственное место, в котором он может быть сконструирован - это метод {@link FListUtils#cons}.
 * Конструирование в других местах будет считаться ошибкой.
 */
final class Cons<T> implements FList<T> {

  private final T head;
  private final FList<? extends T> tail;
  private final int size;

  Cons(T head, FList<? extends T> tail) {
    this.head = head;
    this.tail = tail;
    this.size = 1 + tail.size();
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public T get(int index) {
    Objects.checkIndex(index, size);
    if (index == 0) {
      return head;
    }
    return tail.get(index - 1);
  }

  @Override
  public <R> R foldr(R zero, BiFunction<? super T, R, ? extends R> folder) {
    return folder.apply(head, tail.foldr(zero, folder));
  }

  @Override
  public <R> R foldl(R zero, BiFunction<R, ? super T, ? extends R> folder) {
    return tail.foldl(folder.apply(zero, head), folder);
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(", ", "FList [", "]");
    foldl(joiner, (zero, element) -> zero.add(element.toString()));
    return joiner.toString();
  }


  @Override
  public boolean equals(Object o) {
    if (o instanceof FList<?> other) {
      if (this.size != other.size()) {
        return false;
      }
      // должно работать аж за O(N^2), но как без сохранения элементов во что-то вроде
      // массива реализовать сравнение произвольных FList-ов непонятно.
      for (int i = 0; i < this.size; i++) {
        if (!this.get(i).equals(other.get(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 31 * Objects.hashCode(tail) + Objects.hashCode(head);
  }
}
