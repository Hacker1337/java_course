package org.csc.java.spring2023.iterables;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class IterableUtils {

  /**
   * IterableUtils - утильный класс, мы не хотим, чтобы кто-то мог его инстанционировать.
   */
  private IterableUtils() {
  }

  /**
   * Возвращает объект, позволяющий итерироваться по числам от {@code from} до {@code to}. Шаг
   * итерации всегда равен 1.
   * <p>
   * Ваша реализация должна использовать O(1) дополнительной памяти.
   * <p>
   * Разрешено использовать анонимные и статические классы.
   * <p>
   * Пример использования:
   * <p>
   * <pre>
   * {@code
   * System.out.println("Numbers 1-99:");
   * for (int i : range(0, 100)) {
   *  System.out.println(i);
   * }
   * }
   * </pre>
   * <p>
   * Цена: 0.5 балла
   *
   * @param from начальный индекс
   * @param to   конечный индекс (невключительно)
   * @return итерируемый объект
   */
  public static Iterable<Integer> range(int from, int to) {
    return new RangeIterable(from, to, 1);
  }

  /**
   * Возвращает объект, позволяющий итерироваться по числам от {@code from} до {@code to} с шагом
   * {@code step}.
   * <p>
   * Ваша реализация должна использовать O(1) дополнительной памяти.
   * <p>
   * Разрешено использовать анонимные и статические классы.
   * <p>
   * Пример использования:
   * <pre>
   * {@code
   * System.out.println("Odd numbers:");
   * for (int i : range(1, 100, 2)) {
   *  System.out.println(i);
   * }
   * }
   * </pre>
   * <p>
   * Цена: 0.5 балла
   *
   * @param from начальный индекс
   * @param to   конечный индекс (невключительно)
   * @param step размер шага итерации (любое целое число)
   * @return итерируемый объект
   */
  public static Iterable<Integer> range(int from, int to, int step) {
    return new RangeIterable(from, to, step);
  }

  /**
   * Возвращает объект, позволяющий итерироваться по символам переданной строки.
   * <p>
   * Ваша реализация должна использовать O(1) дополнительной памяти.
   * <p>
   * Разрешено использовать анонимные и статические классы.
   * <p>
   * Пример использования:
   * <p>
   * <pre>
   * {@code
   * for (char c : iterate("Awesome string")) {
   *  if (Character.isUpperCase(c) {
   *    System.out.println(c + " is upper case char");
   *  }
   * }
   * }
   * </pre>
   * <p>
   * Цена: 1 балл
   *
   * @param string строка, по символам которой будет происходить итерирование
   * @return итерируемый объект
   */
  public static Iterable<Character> iterate(String string) {

    class StringIterable implements Iterable<Character> {

      private final String string;

      public StringIterable(String string) {
        this.string = string;
      }

      @Override
      public Iterator<Character> iterator() {
        return new StringIterator(string);
      }

      static class StringIterator implements Iterator<Character> {

        private final String string;
        private int index;

        public StringIterator(String string) {
          this.string = string;
          this.index = -1;
        }

        @Override
        public boolean hasNext() {
          return index + 1 < string.length();
        }

        @Override
        public Character next() {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          index++;
          return string.charAt(index);
        }
      }
    }

    return new StringIterable(string);
  }

  /**
   * Возвращает объект, позволяющий итерироваться одновременно по двум итерируемым объектам. Пусть
   * {@code first} позволяет проитерироваться по n элементам, а {@code second} - по m. Тогда
   * возвращаемый объект позволит проитерироваться по min(n, m) элементам.
   * <p>
   * Разрешено использовать анонимные и статические классы.
   * <p>
   * Пример использования:
   * <p>
   * <pre>
   * {@code
   * List<String> names = List.of("Миша", "Сергей", "Роман");
   * List<Integer> grades = List.of(5, 5, 4, 3);
   * for (Pair<String, Integer> gradedName : zip(names, grades)) {
   *  System.out.println(String.format("%s имеет оценку %d", gradedName.getFirst(), gradedName.getSecond());
   * }
   * }
   * </pre>
   * <p>
   * Цена: 1 балл
   *
   * @param first  первый итерируемый объект
   * @param second второй итерируемый объект
   * @param <A>    тип элементов первого итерируемого объекта
   * @param <B>    тип элементов второго итерируемого объекта
   * @return объект, позволяющий произвести одновременную итерацию
   */
  public static <A, B> Iterable<Pair<A, B>> zip(Iterable<? extends A> first,
      Iterable<? extends B> second) {

    class ZipIterable<A, B> implements Iterable<Pair<A, B>> {

      private final Iterable<? extends A> first;
      private final Iterable<? extends B> second;

      public ZipIterable(Iterable<? extends A> first, Iterable<? extends B> second) {
        this.first = first;
        this.second = second;
      }

      @Override
      public Iterator<Pair<A, B>> iterator() {
        return new ZipIterator<>(first.iterator(), second.iterator());
      }

      class ZipIterator<A, B> implements Iterator<Pair<A, B>> {


        private final Iterator<? extends A> iteratorA;
        private final Iterator<? extends B> iteratorB;

        public ZipIterator(Iterator<? extends A> iteratorA, Iterator<? extends B> iteratorB) {
          this.iteratorA = iteratorA;
          this.iteratorB = iteratorB;
        }

        @Override
        public boolean hasNext() {
          return iteratorA.hasNext() && iteratorB.hasNext();
        }

        @Override
        public Pair<A, B> next() {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          return Pair.of(iteratorA.next(), iteratorB.next());
        }
      }
    }

    return new ZipIterable<>(first, second);
  }

  private static class RangeIterable implements Iterable<Integer> {

    private final int from;
    private final int to;
    private final int step;

    RangeIterable(int from, int to, int step) {
      this.from = from;
      this.to = to;
      this.step = step;
    }

    @Override
    public Iterator<Integer> iterator() {
      return new RangeIterator(from, to, step);
    }

    private static class RangeIterator implements Iterator<Integer> {

      private final int step;
      private final int end;
      private int current;

      RangeIterator(int beg, int end, int step) {
        current = beg - step;
        this.end = end;
        this.step = step;
      }

      @Override
      public boolean hasNext() {
        return ((current + step) - end) * step < 0;
      }

      @Override
      public Integer next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        current += step;
        return current;
      }
    }
  }
}
