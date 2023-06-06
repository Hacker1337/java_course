package org.csc.java.spring2023.bidimap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.csc.java.spring2023.NotImplementedException;
import org.csc.java.spring2023.iterables.Pair;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class HashBidiMapTest {

  @Test
  void testEmpty() {
    BidiMap<Integer, Integer> map = new HashBidiMap<>();
    assertThat(map).isEmpty();
    assertThat(map.get(0)).isNull();
  }

  @Test
  void testMapBased() {
    Map<Integer, Integer> originMap = new HashMap<>();
    originMap.put(5, 10);

    BidiMap<Integer, Integer> map = new HashBidiMap<>(originMap);
    assertThat(map).hasSize(1);
    assertThat(map.get(0)).isNull();
    assertThat(map.get(5)).isEqualTo(10);
    assertThat(map.getKeysByValue(10)).containsOnly(5);
    assertThat(map.getKeysByValue(5)).isNull();
  }

  @Test
  void getKeysByValueResultCannotBeModified() {
    BidiMap<Integer, String> map = new HashBidiMap<>(Map.of(
        1, "hello",
        2, "hello"
    ));

    List<Integer> keys = map.getKeysByValue("hello");
    assertThat(keys).contains(1, 2);

    assertThrows(
        RuntimeException.class,
        () -> keys.add(3),
        "Список, возвращаемый из getKeysByValue, не должен поддаваться модификациям. "
            + "Для решения этой проблемы обратитесь к классу java.util.Collections"
    );
  }

  @Nested
  class Remove {

    @Test
    void testRemoveFromOrigin() {
      Map<Integer, Integer> originMap = new HashMap<>();

      originMap.put(5, 10);
      BidiMap<Integer, Integer> map = new HashBidiMap<>(originMap);

      originMap.remove(5);
      assertThat(map).hasSize(1);
      assertThat(map.get(5)).isEqualTo(10);
    }

    @Test
    void testRemove() {
      Map<Integer, Integer> originMap = new HashMap<>();

      originMap.put(5, 10);
      BidiMap<Integer, Integer> map = new HashBidiMap<>(originMap);

      map.remove(5);
      assertThat(map).isEmpty();
      assertThat(map.get(5)).isNull();
      assertThat(originMap.get(5)).isEqualTo(10);
    }

    @Test
    void testRemoveViaIterator() {
      Assumptions.assumeTrue(bidiMapSupportsRemovingViaIterator(),
          "HashBidiMapEntrySet.EntryIterator.remove не реализован");

      BidiMap<Integer, String> map = new HashBidiMap<>(Map.of(
          1, "one",
          2, "two",
          3, "three",
          4, "four",
          5, "five",
          6, "six"
      ));

      assertThat(map).hasSize(6);

      Iterator<Entry<Integer, String>> iterator = map.entrySet().iterator();

      //noinspection Java8CollectionRemoveIf
      while (iterator.hasNext()) {
        Entry<Integer, String> current = iterator.next();

        if (current.getKey() % 2 == 0 || current.getValue().equals("five")) {
          iterator.remove();
        }
      }

      assertThat(map).hasSize(2);

      assertThat(map.get(1)).isEqualTo("one");
      assertThat(map.get(2)).isNull();
      assertThat(map.get(3)).isEqualTo("three");
      assertThat(map.get(4)).isNull();
      assertThat(map.get(5)).isNull();
      assertThat(map.get(6)).isNull();

      assertThat(map.getKeysByValue("two")).isNull();
    }

  }

  @Test
  void testPut() {
    BidiMap<Integer, Integer> map = new HashBidiMap<>();

    map.put(5, 10);
    assertThat(map.get(5)).isEqualTo(10);
    assertThat(map.getKeysByValue(10)).containsOnly(5);
    assertThat(map).hasSize(1);

    map.put(6, 10);
    assertThat(map.get(6)).isEqualTo(10);
    assertThat(map.getKeysByValue(10)).containsOnly(5, 6);
    assertThat(map).hasSize(2);

    map.put(5, 15);
    map.put(6, 15);
    assertThat(map.get(5)).isEqualTo(15);
    assertThat(map.get(6)).isEqualTo(15);
    assertThat(map.getKeysByValue(10)).isNull();
    assertThat(map).hasSize(2);
  }

  @Nested
  class RemoveByValue {

    @Test
    void basicRemovals() {
      BidiMap<Integer, Integer> map = new HashBidiMap<>();

      map.put(5, 10);
      map.put(6, 10);
      map.put(7, 11);
      assertThat(map).hasSize(3);

      List<Integer> removedKeys = map.removeByValue(10);
      assertThat(removedKeys).hasSize(2);
      assertThat(removedKeys).contains(5, 6);

      assertThat(map).hasSize(1);

      assertThat(map.get(5)).isNull();
      assertThat(map.get(6)).isNull();
      assertThat(map.get(7)).isEqualTo(11);

      assertThat(map.getKeysByValue(10)).isNull();
      assertThat(map.getKeysByValue(11)).containsOnly(7);
    }

    @Test
    void removeFromEmpty() {
      HashBidiMap<Integer, String> map = new HashBidiMap<>();

      List<Integer> removedKeys = map.removeByValue("hello");

      assertThat(removedKeys).isNull();
    }

    @Test
    void removeByValueResultCannotBeModified() {
      HashBidiMap<Integer, String> map = new HashBidiMap<>(Map.of(
          1, "hello",
          2, "hello"
      ));

      List<Integer> removedKeys = map.removeByValue("hello");
      assertThat(removedKeys).contains(1, 2);

      assertThrows(
          RuntimeException.class,
          () -> removedKeys.add(10),
          "Список, возвращаемый из removeByValue, не должен поддаваться модификациям"
              + "Для решения этой проблемы обратитесь к классу java.util.Collections"
      );
    }
  }

  @Test
  void testDifferentEqualObjects() {
    // Pair class has overridden `equals` method
    Pair<Integer, Integer> pair1 = Pair.of(1, 2);
    Pair<Integer, Integer> pair2 = Pair.of(1, 2);

    BidiMap<Integer, Pair<Integer, Integer>> map = new HashBidiMap<>();

    map.put(1, pair1);
    map.put(2, pair2);
    assertThat(map.getKeysByValue(Pair.of(1, 2))).containsOnly(1, 2);
    assertThat(map).isNotEmpty();

    map.removeByValue(Pair.of(1, 2));
    assertThat(map).isEmpty();
  }

  /**
   * Проверяет, реализован ли метод HashBidiMapEntrySet.EntryIterator.remove в работе студента.
   */
  private boolean bidiMapSupportsRemovingViaIterator() {
    BidiMap<Integer, String> map = new HashBidiMap<>(Map.of(1, "one", 2, "two"));

    Iterator<Entry<Integer, String>> iterator = map.entrySet().iterator();
    assertThat(iterator.hasNext()).isTrue();

    // move to the first item
    iterator.next();

    try {
      iterator.remove();

      return true;
    } catch (NotImplementedException e) {
      return false;
    }
  }
}
