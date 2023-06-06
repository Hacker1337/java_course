package org.csc.java.spring2023.immutable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * Этот класс является точкой входа для работы с {@link FList}. Все операции кроме примитивных или
 * Java-специфичных (equals, toString, hashCode) должны проводиться с использованием статических
 * методов этого класса.
 * <p>
 * Вам необходимо реализовать все нереализованные методы, которые есть в этом классе. Для их
 * реализации постарайтесь по-максимуму использовать функции {@link FList#foldl} и
 * {@link FList#foldr}; старайтесь избегать циклов и мутируемых переменных, иначе проверяющий может
 * попросить вас переделать решение.
 * <p>
 * В качестве примера оставлена реализация метода {@link FListUtils#forEach} с использованием
 * foldl.
 */
public final class FListUtils {

  /**
   * Возвращает пустой список (см. {@link Nil}).
   */
  public static <T> FList<T> empty() {
    return new Nil<>();
  }

  /**
   * Создает новый список на основе head и tail.
   * <p>
   * Получившийся список должен иметь head в качестве первого элемента, и список tail в качестве
   * хвоста (см. {@link Cons}).
   */
  public static <T> FList<T> cons(T head, FList<? extends T> tail) {
    return new Cons<>(head, tail);
  }

  /**
   * Возвращает список, содержащий элементы из elements в оригинальном порядке.
   * <p>
   * Позволяет удобно создать список сразу из нескольких элементов.
   * <p>
   * Полученный список не должен отличаться от списка, созданного из тех же элементов с помощью
   * нескольких вызовов {@link FListUtils#cons}.
   */
  @SafeVarargs
  public static <T> FList<T> listOf(T... elements) {
    FList<T> result = empty();
    for (int i = elements.length - 1; i >= 0; i--) {
      result = cons(elements[i], result);
    }
    return result;
  }

  /**
   * <p>
   * Операция правой свертки.
   * </p>
   * <p>
   * Пример: {@code foldr([1, 2, 3, 4], 42, f) == f(1, f(2, f(3, f(4, 42))))}.
   * </p>
   * <p>
   * Несколько примеров есть в тестах.
   * </p>
   */
  public static <T, R> R foldr(
      FList<? extends T> list,
      R zero,
      BiFunction<? super T, R, ? extends R> folder
  ) {
    return list.foldr(zero, folder);
  }

  /**
   * <p>
   * Операция левой свертки.
   * </p>
   * <p>
   * Пример: {@code foldl([1, 2, 3, 4], 42, f) == f(f(f(f(42, 1), 2), 3), 4)}.
   * </p>
   * <p>
   * Несколько примеров есть в тестах.
   * </p>
   */
  public static <T, R> R foldl(
      FList<? extends T> list,
      R zero,
      BiFunction<R, ? super T, ? extends R> folder
  ) {
    return list.foldl(zero, folder);
  }

  /**
   * Создает обычный Java-список на основе переданного списка.
   * <p>
   * Этот метод нужен для более удобного взаимодействия с существующим Java-кодом.
   * <p>
   * Этот метод должен работать за O(N).
   * <p>
   * <b>ВАЖНО</b>: Он не должен использоваться нигде в вашей реализации! Его могут вызывать только
   * тесты!
   */
  public static <T> List<T> toJavaList(FList<T> list) {
    ArrayList<T> result = new ArrayList<>();
    forEach(list, result::add);
    return result;
  }

  /**
   * Объединяет два списка в один: сначала идут элементы из first, потом из second.
   * <p>
   * Этот метод должен работать за O(first.size()).
   */
  public static <T> FList<T> concat(FList<? extends T> first, FList<T> second) {
    return foldr(first, second, FListUtils::cons);
  }

  /**
   * Выполняет action на каждом элементе списка по-порядку.
   * <p>
   * Вызывается ради побочных эффектов, например для печати содержимого в консоль.
   */
  public static <T> void forEach(FList<? extends T> list, Consumer<? super T> action) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(action);
    foldl(list, null, (nothing, element) -> {
      action.accept(element);
      return nothing;
    });
  }

  /**
   * Создает новый список на основе уже существующего, применяя mapper к каждому элементу
   * оригинального списка (оригинал остается неизменным).
   * <p>
   * Используется для однотипных преобразований сразу всех элементов списка.
   */
  public static <T, K> FList<K> map(FList<? extends T> list,
      Function<? super T, ? extends K> mapper) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(mapper);
    return foldr(list, empty(), (element, collector) -> cons(mapper.apply(element), collector));
  }

  /**
   * Создает новый список из уже существующего, оставляя только те элементы, которые удовлетворяют
   * предикату filterFunc.
   */
  public static <T> FList<T> filter(FList<? extends T> list, Predicate<? super T> filterFunc) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(filterFunc);
    return foldr(list, empty(), (element, collector) -> {
      if (filterFunc.test(element)) {
        return cons(element, filter(collector, filterFunc));
      }
      return filter(collector, filterFunc);
    });
  }
}