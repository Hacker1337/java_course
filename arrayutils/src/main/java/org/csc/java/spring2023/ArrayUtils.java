package org.csc.java.spring2023;


import java.util.Objects;

/**
 * Суммарное число баллов -- 10.
 */
public final class ArrayUtils {

  private ArrayUtils() {
  }

  /**
   * Печатает массив в `stdout` в формате `[1, 2, 3]`. Не добавляет переносов строк.
   * <br>
   * Цена: 1 балл
   *
   * @param array массив для печати
   */
  public static void printArray(int[] array) {
    Objects.requireNonNull(array);
    if (array.length == 0) {
      System.out.print("[]");
      return;
    }

    System.out.print('[');
    for (int i = 0; i < array.length - 1; i++) {
      System.out.print(array[i]);
      System.out.print(", ");
    }
    System.out.print(array[array.length - 1]);
    System.out.print(']');
  }

  /**
   * Печатает массив в `stdout` в формате `[\n[1, 2, 3],\n[1, 2],\n[1, 2, 3, 4]\n]`. \n - это
   * переносы строк. Для создания переноса строк используйте System.out.println(), не печатайте \n
   * напрямую. Это помешает нашим тестам выполняться на Windows.
   * <br>
   * Если array.length == 0, то должно напечатать `[]` без переносов строк.
   * <br>
   * Если array.length == 1, то должно напечатать в формате `[[1, 2, 3]]` без переносов.
   * <br>
   * <br>
   * Цена: 1 балл
   *
   * @param array массив для печати
   */
  public static void printArray(int[][] array) {
    if (array.length == 0) {
      System.out.print("[]");
      return;
    }
    if (array.length == 1) {
      // short array without line breaks
      System.out.print('[');
      printArray(array[0]);
      System.out.print(']');
      return;
    }

    System.out.println('[');
    for (int i = 0; i < array.length - 1; i++) {
      printArray(array[i]);
      System.out.println(',');
    }
    printArray(array[array.length - 1]);
    System.out.println();
    System.out.print(']');


  }

  /**
   * Создать копию промежутка из оригинального массива.
   * <br>
   * Требования функции: array != null, 0 <= fromInclusive <= toExclusive <= array.length.
   * <br>
   * Цена: 1 балл
   *
   * @param array         оригинальный массив.
   * @param fromInclusive начальный индекс промежутка.
   * @param toExclusive   конечный индекс промежутка (не включительно).
   * @return копия промежутка [fromInclusive, toExclusive)
   */
  public static int[] subArray(int[] array, int fromInclusive, int toExclusive) {
    Objects.requireNonNull(array);
    Objects.checkFromToIndex(fromInclusive, toExclusive, array.length);

    int[] result = new int[toExclusive - fromInclusive];
    if (toExclusive - fromInclusive >= 0) {
      System.arraycopy(array, fromInclusive, result, 0,
          result.length);
    }

    return result;
  }

  /**
   * Разбивает массив на несколько равных кусков.
   * <br>
   * Цена: 1 балл
   *
   * @param array     исходный массив
   * @param chunkSize размер одного куска
   * @return массив из отдельных кусков
   */
  public static int[][] chunked(int[] array, int chunkSize) {
    if (chunkSize <= 0) {
      throw new IllegalArgumentException("chunkSize should be > 0");
    }
    int chunksNumber = (int) Math.ceil(((double) array.length) / chunkSize);
    int[][] result = new int[chunksNumber][];
    for (int i = 0; i < array.length; i += chunkSize) {
      result[i / chunkSize] = subArray(array, i, Math.min(i + chunkSize, array.length));
    }
    return result;
  }

  /**
   * Объединить два отсортированных массива в один отсортированный массив.
   * <br>
   * Цена: 1 балл
   *
   * @param left  первый отсортированный массив.
   * @param right второй отсортированный массив.
   * @return массив, объединенный из left и right.
   */
  public static int[] mergeSortedArrays(int[] left, int[] right) {
    int[] merged = new int[left.length + right.length];
    int leftPos = 0;
    int rightPos = 0;
    while (leftPos < left.length && rightPos < right.length) {
      if (left[leftPos] <= right[rightPos]) {
        merged[leftPos + rightPos] = left[leftPos++];
      } else {
        merged[leftPos + rightPos] = right[rightPos++];
      }
    }
    // add the rest of data without comparing, when one of arrays is empty

    System.arraycopy(left, leftPos, merged, leftPos + rightPos, left.length - leftPos);
    System.arraycopy(right, rightPos, merged, leftPos + rightPos, right.length - rightPos);

    return merged;
  }

  /**
   * Создает копию массива array и сортирует её, используя сортировку слиянием.
   * <br>
   * Цена: 2 балла
   *
   * @param array исходный массив.
   * @return копию исходного массива, но с отсортированными элементами.
   */
  public static int[] sorted(int[] array) {
    if (array.length <= 1) {
      return array.clone();
    }

    int mid = array.length / 2;

    int[] left = sorted(subArray(array, 0, mid));
    int[] right = sorted(subArray(array, mid, array.length));

    return mergeSortedArrays(left, right);
  }

  /**
   * Переворачивает содержимое массива `array` на промежутке [fromInclusive, toExclusive).
   * <br>
   * Требования функции: array != null, 0 <= fromInclusive <= toExclusive <= array.length.
   * <br>
   * Функция должна работать за O(toExclusive - fromExclusive) и не выделять дополнительной памяти
   * (не использовать оператор <code>new</code>).
   * <br>
   * Цена: 1 балл
   *
   * @param array         массив, который нужно перевернуть.
   * @param fromInclusive индекс начала промежутка.
   * @param toExclusive   индекс конца промежутка (не включительно).
   */

  public static void reverseRange(int[] array, int fromInclusive, int toExclusive) {
    Objects.checkFromToIndex(fromInclusive, toExclusive, array.length);
    Objects.requireNonNull(array);

    int len = (toExclusive - fromInclusive);
    int mid = (toExclusive + fromInclusive) / 2;
    int i = fromInclusive;
    while (i < mid) {
      int j = fromInclusive + len - (i - fromInclusive) - 1; // symmetric relative to mid
      swap(array, i, j);
      i++;
    }
  }

  private static void swap(int[] array, int i, int j) {
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Делает циклический сдвиг содержимого массива на steps шагов вправо.
   * <br>
   * Функция должна поддерживать отрицательное количество шагов, работать за O(N) и не выделять
   * дополнительной памяти (не использовать оператор <code>new</code>).
   * <br>
   * Примеры:
   * <ul>
   * <li>rotate({1, 2, 3}, 0) -> {1, 2, 3}</li>
   * <li>rotate({1, 2, 3}, 0) -> {1, 2, 3}</li>
   * <li>rotate({1, 2, 3}, 1) -> {3, 1, 2}</li>
   * <li>rotate({1, 2, 3}, -1) -> {2, 3, 1}</li>
   * <li>rotate({1, 2, 3}, 3) -> {1, 2, 3}</li>
   * <li>rotate({1, 2, 3}, 3001) -> {3, 1, 2}</li>
   * </ul>
   * <br>
   * Требования функции: array != null.
   * <br>
   * Цена: 2 балла
   *
   * @param array массив, на котором нужно сделать циклический сдвиг.
   * @param steps количество шагов, на которое нужно сделать сдвиг.
   */
  public static void rotate(int[] array, int steps) {
    Objects.requireNonNull(array);
    if (array.length == 0) {
      return;
    }

    // changes steps to be in [0, array.length)
    int simpleSteps = steps % array.length;
    if (simpleSteps < 0) {
      simpleSteps += array.length;
    }

    reverseRange(array, 0, array.length);
    reverseRange(array, 0, simpleSteps);
    reverseRange(array, simpleSteps, array.length);

  }
}
