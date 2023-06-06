package org.csc.java.spring2023;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ArrayUtilsTest {

  @Test
  void printArray_smoke() {
    assertEquals("[]", printArrayToString(new int[]{}));

    assertEquals("[1]", printArrayToString(new int[]{1}));
    assertEquals("[1, 2, 3]", printArrayToString(new int[]{1, 2, 3}));
    assertEquals("[1, 2, 3]", printArrayToString(new int[]{1, 2, 3}));

    assertEquals("[2147483647]",
        printArrayToString(new int[]{Integer.MAX_VALUE}));

    assertRejectsArgument(
        () -> ArrayUtils.printArray((int[]) null),
        "Null array should be prohibited!"
    );
  }

  @Test
  void print2DArray_smoke() {
    assertEqualsWithCorrectLineSeparator("[]", print2DArrayToString(new int[][]{}));

    assertEqualsWithCorrectLineSeparator("[[1]]", print2DArrayToString(new int[][]{{1}}));
    assertEqualsWithCorrectLineSeparator("[\n[1],\n[2],\n[3]\n]",
        print2DArrayToString(new int[][]{{1}, {2}, {3}}));

    assertEqualsWithCorrectLineSeparator("[\n[4, 3, 2, 1],\n[],\n[1, 2, 3]\n]",
        print2DArrayToString(new int[][]{{4, 3, 2, 1}, {}, {1, 2, 3}}));

    assertEqualsWithCorrectLineSeparator("[[2147483647]]",
        print2DArrayToString(new int[][]{{Integer.MAX_VALUE}}));

    assertRejectsArgument(
        () -> ArrayUtils.printArray((int[][]) null),
        "Null array should be prohibited!"
    );

    assertRejectsArgument(
        () -> ArrayUtils.printArray((new int[][]{null})),
        "Null array should be prohibited!"
    );
  }

  @Test
  void subArray_smoke() {
    assertArrayIsEmpty(ArrayUtils.subArray(new int[]{}, 0, 0));

    assertArrayIsEmpty(ArrayUtils.subArray(new int[]{1, 2, 3}, 0, 0));
    assertArrayIsEmpty(ArrayUtils.subArray(new int[]{1, 2, 3}, 1, 1));
    assertArrayIsEmpty(ArrayUtils.subArray(new int[]{1, 2, 3}, 2, 2));

    assertArrayEquals(new int[]{1}, ArrayUtils.subArray(new int[]{1, 2, 3}, 0, 1));
    assertArrayEquals(new int[]{1, 2}, ArrayUtils.subArray(new int[]{1, 2, 3}, 0, 2));
    assertArrayEquals(new int[]{1, 2, 3}, ArrayUtils.subArray(new int[]{1, 2, 3}, 0, 3));

    assertRejectsArgument(() -> ArrayUtils.subArray(null, 0, 0),
        "Null array should be prohibited!");

    assertRejectsArgument(() -> ArrayUtils.subArray(new int[]{1, 2, 3}, -1, 0),
        "Start of range must be >= 0");
    assertRejectsArgument(() -> ArrayUtils.subArray(new int[]{1, 2, 3}, 2, 1),
        "Start of range must be <= end of range");
    assertRejectsArgument(() -> ArrayUtils.subArray(new int[]{1, 2, 3}, 0, 4),
        "End of range must be <= array.length");
  }

  @Test
  void chunked_smoke() {
    assertEquals(0, ArrayUtils.chunked(new int[]{}, 10).length);

    assertArrayEquals(
        new int[][]{{1}, {2}, {3}},
        ArrayUtils.chunked(new int[]{1, 2, 3}, 1)
    );
    assertArrayEquals(
        new int[][]{{1, 2}, {3}},
        ArrayUtils.chunked(new int[]{1, 2, 3}, 2)
    );
    assertArrayEquals(
        new int[][]{{1, 2, 3}},
        ArrayUtils.chunked(new int[]{1, 2, 3}, 10)
    );

    assertArrayEquals(
        new int[][]{{1, 2}, {3, 4}, {5}},
        ArrayUtils.chunked(new int[]{1, 2, 3, 4, 5}, 2)
    );

    assertRejectsArgument(() -> ArrayUtils.chunked(null, 1), "Null array should be prohibited!");
    assertRejectsArgument(() -> ArrayUtils.chunked(new int[]{}, 0), "Chunk size should be > 0");
    assertRejectsArgument(() -> ArrayUtils.chunked(new int[]{}, -1), "Chunk size should be > 0");
  }

  @Test
  void mergeSortedArray_smoke() {
    assertArrayIsEmpty(ArrayUtils.mergeSortedArrays(new int[]{}, new int[]{}));

    assertArrayEquals(
        new int[]{1, 2, 3},
        ArrayUtils.mergeSortedArrays(new int[]{1, 2, 3}, new int[]{})
    );
    assertArrayEquals(
        new int[]{4, 5, 6},
        ArrayUtils.mergeSortedArrays(new int[]{}, new int[]{4, 5, 6})
    );

    assertArrayEquals(
        new int[]{1, 2, 3, 4, 5, 6},
        ArrayUtils.mergeSortedArrays(new int[]{1, 2, 3}, new int[]{4, 5, 6})
    );
    assertArrayEquals(
        new int[]{1, 2, 3, 4, 5, 6},
        ArrayUtils.mergeSortedArrays(new int[]{4, 5, 6}, new int[]{1, 2, 3})
    );
    assertArrayEquals(
        new int[]{1, 2, 3, 4, 5, 6},
        ArrayUtils.mergeSortedArrays(new int[]{1, 3, 6}, new int[]{2, 4, 5})
    );

    assertRejectsArgument(() -> ArrayUtils.mergeSortedArrays(null, new int[]{}),
        "Null array should be prohibited!");
    assertRejectsArgument(() -> ArrayUtils.mergeSortedArrays(new int[]{}, null),
        "Null array should be prohibited!");
  }

  @Test
  void sorted_smoke() {
    assertArrayEqualsAfterMergeSort(new int[]{}, new int[]{});
    assertArrayEqualsAfterMergeSort(new int[]{1}, new int[]{1});
    assertArrayEqualsAfterMergeSort(new int[]{1, 1, 2, 3}, new int[]{3, 1, 2, 1});
    assertArrayEqualsAfterMergeSort(
        new int[]{1, 2, 3, 4, 5, 6},
        new int[]{6, 5, 4, 3, 2, 1}
    );
    assertArrayEqualsAfterMergeSort(
        new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
        new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}
    );

    assertRejectsArgument(() -> ArrayUtils.sorted(null), "Null array should be prohibited!");

    {
      String copyExpectedMessage = "Sorting should always return a copy of array!";

      int[] empty = {};
      assertNotSame(empty, ArrayUtils.sorted(empty), copyExpectedMessage);

      int[] singleElement = {1};
      assertNotSame(singleElement, ArrayUtils.sorted(singleElement), copyExpectedMessage);

      int[] twoSortedElements = {1, 2};
      assertNotSame(twoSortedElements, ArrayUtils.sorted(twoSortedElements), copyExpectedMessage);
    }
  }

  @Test
  void reverseRange_smoke() {
    assertEqualsAfterReverse(new int[]{}, new int[]{}, 0, 0);

    assertEqualsAfterReverse(new int[]{1}, new int[]{1}, 0, 1);

    assertEqualsAfterReverse(new int[]{1, 2, 3}, new int[]{1, 2, 3}, 0, 1);
    assertEqualsAfterReverse(new int[]{1, 2, 3}, new int[]{1, 2, 3}, 1, 2);
    assertEqualsAfterReverse(new int[]{1, 2, 3}, new int[]{1, 2, 3}, 2, 3);

    assertEqualsAfterReverse(new int[]{2, 1, 3, 4}, new int[]{1, 2, 3, 4}, 0, 2);
    assertEqualsAfterReverse(new int[]{1, 3, 2, 4}, new int[]{1, 2, 3, 4}, 1, 3);
    assertEqualsAfterReverse(new int[]{1, 2, 4, 3}, new int[]{1, 2, 3, 4}, 2, 4);

    assertEqualsAfterReverse(new int[]{3, 2, 1}, new int[]{1, 2, 3}, 0, 3);
    assertEqualsAfterReverse(new int[]{4, 3, 2, 1}, new int[]{1, 2, 3, 4}, 0, 4);

    assertRejectsArgument(
        () -> ArrayUtils.reverseRange(null, 0, 0),
        "Null array should be prohibited!"
    );

    assertRejectsArgument(
        () -> ArrayUtils.reverseRange(new int[]{}, -1, 0),
        "Null array should be prohibited!"
    );

    assertRejectsArgument(
        () -> ArrayUtils.reverseRange(new int[]{}, -1, 0),
        "Start of range must be >= 0"
    );

    assertRejectsArgument(
        () -> ArrayUtils.reverseRange(new int[]{1, 2}, 2, 1),
        "Start of range must be <= end of range"
    );

    assertRejectsArgument(
        () -> ArrayUtils.reverseRange(new int[]{1, 2, 3}, 0, 4),
        "End of range must be <= array.length"
    );
  }

  @Test
  void rotate_smoke() {
    assertEqualsAfterRotation(new int[]{}, new int[]{}, 0);
    assertEqualsAfterRotation(new int[]{}, new int[]{}, 1);
    assertEqualsAfterRotation(new int[]{}, new int[]{}, 100);
    assertEqualsAfterRotation(new int[]{}, new int[]{}, -100);

    assertEqualsAfterRotation(new int[]{1, 2, 3}, new int[]{1, 2, 3}, 0);

    assertEqualsAfterRotation(new int[]{3, 1, 2}, new int[]{1, 2, 3}, 1);
    assertEqualsAfterRotation(new int[]{2, 3, 1}, new int[]{1, 2, 3}, 2);
    assertEqualsAfterRotation(new int[]{1, 2, 3}, new int[]{1, 2, 3}, 3);
    assertEqualsAfterRotation(new int[]{3, 1, 2}, new int[]{1, 2, 3}, 4);

    assertEqualsAfterRotation(new int[]{2, 3, 1}, new int[]{1, 2, 3}, -1);
    assertEqualsAfterRotation(new int[]{3, 1, 2}, new int[]{1, 2, 3}, -2);
    assertEqualsAfterRotation(new int[]{1, 2, 3}, new int[]{1, 2, 3}, -3);
    assertEqualsAfterRotation(new int[]{2, 3, 1}, new int[]{1, 2, 3}, -4);

    assertEqualsAfterRotation(new int[]{3, 4, 5, 6, 7, 8, 1, 2}, new int[]{1, 2, 3, 4, 5, 6, 7, 8}, -2);
  }

  private String printArrayToString(int[] array) {
    return withReplacedSout(() -> ArrayUtils.printArray(array));
  }

  private String print2DArrayToString(int[][] array) {
    return withReplacedSout(() -> ArrayUtils.printArray(array));
  }

  /**
   * Runs runnable with replaces System.out and then returns what have been printed there.
   */
  private String withReplacedSout(Runnable runnable) {
    PrintStream systemOut = System.out;

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (PrintStream tmpOut = new PrintStream(bytes)) {
      System.setOut(tmpOut);
      runnable.run();
    } finally {
      System.setOut(systemOut);
    }

    return bytes.toString();
  }

  private void assertEqualsWithCorrectLineSeparator(String expected, String actual) {
    assertEquals(expected.replace("\n", System.lineSeparator()), actual);
  }

  /**
   * Проверяет, что переданный массив пуст.
   */
  private void assertArrayIsEmpty(int[] array) {
    assertEquals(0, array.length);
  }

  /**
   * Проверяет, что при выполнении executable выбросит исключение, сигнализируя о
   * неправильном аргументе, переданном в функцию внутри executable.
   */
  private static void assertRejectsArgument(Executable executable, String message) {
    assertThrows(Exception.class, executable, message);
  }

  /**
   * Проверяет, что если массив beforeSorting отсортировать, то его содержимое будет соответствовать
   * expected.
   */
  private static void assertArrayEqualsAfterMergeSort(int[] expected, int[] beforeSorting) {
    int[] sortedArray = ArrayUtils.sorted(beforeSorting);

    assertArrayEquals(
        expected,
        sortedArray,
        "Expected array " + Arrays.toString(beforeSorting)
            + " to be equal to " + Arrays.toString(expected)
            + " after sorting, but got " + Arrays.toString(sortedArray)
    );
  }

  private void assertEqualsAfterReverse(
      int[] expected,
      int[] initial,
      int fromInclusive,
      int toExclusive
  ) {
    int[] initialArrayCopy = initial.clone();
    ArrayUtils.reverseRange(initialArrayCopy, fromInclusive, toExclusive);

    assertArrayEquals(
        expected,
        initialArrayCopy,
        String.format(
            "Expected array %s to be equal to %s after reversing [%d, %d) range, but got %s",
            Arrays.toString(initial), Arrays.toString(expected),
            fromInclusive, toExclusive,
            Arrays.toString(initialArrayCopy))
    );
  }

  private void assertEqualsAfterRotation(
      int[] expected,
      int[] initial,
      int rotationSteps
  ) {
    int[] initialArrayCopy = initial.clone();
    ArrayUtils.rotate(initialArrayCopy, rotationSteps);

    assertArrayEquals(
        expected,
        initialArrayCopy,
        String.format(
            "Expected array %s to be equal to %s after rotating on %d steps, but got %s",
            Arrays.toString(initial), Arrays.toString(expected),
            rotationSteps,
            Arrays.toString(initialArrayCopy))
    );
  }
}
