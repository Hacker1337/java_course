package org.csc.java.spring2023.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class IterableUtilsTest {

  @Test
  void testRange() {
    List<Integer> indices = new ArrayList<>();
    for (int i : IterableUtils.range(11, 20)) {
      indices.add(i);
    }

    assertEquals(9, indices.size());
    for (int i = 0; i < indices.size(); i++) {
      assertThat(indices).element(i).isEqualTo(i + 11);
    }
  }

  @Test
  void testEmptyRange1() {
    List<Integer> indices = new ArrayList<>();
    for (int i : IterableUtils.range(11, 11)) {
      indices.add(i);
    }

    assertThat(indices).isEmpty();
  }

  @Test
  void testEmptyRange2() {
    List<Integer> indices = new ArrayList<>();
    for (int i : IterableUtils.range(11, 0)) {
      indices.add(i);
    }

    assertThat(indices).isEmpty();
  }
  @Test
  void testEmptyRange3() {
    Iterable<Integer> emptyRange = IterableUtils.range(11, 0);
    Iterator<Integer> emptyIterator = emptyRange.iterator();

    assertIteratorIsEmpty(emptyIterator);
  }

  @Test
  void testRangeWithStep() {
    List<Integer> indices = new ArrayList<>();
    for (int i : IterableUtils.range(11, 20, 2)) {
      indices.add(i);
    }

    assertThat(indices).hasSize(5);

    int i = 0;
    for (int forIndex = 11; forIndex < 20; forIndex += 2) {
      assertThat(indices).element(i).isEqualTo(forIndex);
      i++;
    }
  }

  @Test
  void testRangeWithStepBackwards() {
    List<Integer> indices = new ArrayList<>();
    for (int i : IterableUtils.range(10, 0, -2)) {
      indices.add(i);
    }

    assertThat(indices).hasSize(5);

    int i = 0;
    for (int forIndex = 10; forIndex > 0; forIndex -= 2) {
      assertThat(indices).element(i).isEqualTo(forIndex);
      i++;
    }
  }

  @Test
  void testStringIterate() {
    String string = "The quick brown fox jumps over the lazy dog";

    int count = 0;
    for (char c : IterableUtils.iterate(string)) {
      assertThat(c)
          .withFailMessage(
              "Символ %c не равен символу %c по индексу %d", c, string.charAt(count), count
          )
          .isEqualTo(string.charAt(count));

      count++;
    }

    assertThat(count).isEqualTo(string.length());
  }

  @Test
  void testStringIterateEmpty() {
    String string = "";

    var emptyStringIterate = IterableUtils.iterate(string);
    var emptyIterator = emptyStringIterate.iterator();

    assertIteratorIsEmpty(emptyIterator);
  }

  @Test
  void testZip() {
    List<Integer> integers = new ArrayList<>();
    List<String> strings = new ArrayList<>();
    Map<Integer, String> map = new HashMap<>();

    integers.add(2016);
    strings.add("Kotlin release");
    map.put(2016, "Kotlin release");

    integers.add(2002);
    strings.add("Spring release");
    map.put(2002, "Spring release");

    integers.add(1996);
    strings.add("Java release");
    map.put(1996, "Java release");

    strings.add("Unknown date");

    int count = 0;
    for (Pair<Integer, String> pair : IterableUtils.zip(integers, strings)) {
      assertThat(pair.getSecond()).isEqualTo(map.get(pair.getFirst()));
      count++;
    }

    assertThat(count).isEqualTo(Math.min(integers.size(), strings.size()));
  }

  @Test
  void testZipReusability() {
    var numbers = IterableUtils.range(1, 4);

    var zip = IterableUtils.zip(numbers, numbers);

    Iterator<Pair<Integer, Integer>> first = zip.iterator();
    Iterator<Pair<Integer, Integer>> second = zip.iterator();

    for (int i = 0; i < 3; i++) {
      assertThat(first).hasNext();
      assertThat(second).hasNext();

      assertThat(first.next()).isEqualTo(second.next());
    }

    assertIteratorIsEmpty(first);
    assertIteratorIsEmpty(second);
  }

  private static <T> void assertIteratorIsEmpty(Iterator<T> iterator) {
    assertThat(iterator).isExhausted();
    assertThatThrownBy(() -> iterator.next()).isInstanceOf(NoSuchElementException.class);
  }
}
