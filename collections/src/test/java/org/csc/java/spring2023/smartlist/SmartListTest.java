package org.csc.java.spring2023.smartlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class SmartListTest {

  @Test
  public void testEmpty() {
    SmartList<Integer> smartList = new SmartList<>();

    assertThat(smartList).isEmpty();
    assertThrows(IndexOutOfBoundsException.class, () -> smartList.get(0));
  }

  @Test
  public void testSingleElementConstructor() {
    SmartList<Integer> smartList = new SmartList<>(6);

    assertThat(smartList.get(0)).isEqualTo(6);
    assertThat(smartList).hasSize(1);
    assertThrows(IndexOutOfBoundsException.class, () -> smartList.get(1));
  }

  @Test
  public void testCollectionConstructor() {
    List<Integer> arrayList = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < 100; ++i) {
      arrayList.add(random.nextInt());
    }
    SmartList<Integer> smartList = new SmartList<>(arrayList);

    assertThat(smartList).isEqualTo(arrayList);
    assertThrows(IndexOutOfBoundsException.class, () -> smartList.get(100));
  }

  @Test
  public void testVarargConstructor() {
    {
      Integer[] array = new Integer[100];
      Random random = new Random();
      for (int i = 0; i < 100; ++i) {
        array[i] = random.nextInt();
      }

      List<Integer> smartListArray = new SmartList<>(array);

      for (int i = 0; i < 100; ++i) {
        assertThat(smartListArray.get(i)).isEqualTo(array[i]);
      }
      assertThat(smartListArray).hasSize(100);
      assertThrows(IndexOutOfBoundsException.class, () -> smartListArray.get(100));
    }

    {
      List<Integer> smartListVararg = new SmartList<>(1, 2, 3);
      for (int i = 0; i < 3; ++i) {
        assertThat(smartListVararg.get(i)).isEqualTo(i + 1);
      }
      assertThat(smartListVararg).hasSize(3);
      assertThrows(IndexOutOfBoundsException.class, () -> smartListVararg.get(3));
    }
  }

  @Test
  public void testAdd() {
    SmartList<Integer> smartList = new SmartList<>();
    assertThat(smartList).isEmpty();
    smartList.add(1);
    assertThat(smartList).hasSize(1);
    assertThat(smartList.get(0)).isEqualTo(1);
    smartList.add(2);
    assertThat(smartList).hasSize(2);
    assertThat(smartList).containsExactly(1, 2);
  }

  @Test
  public void testRemove() {
    SmartList<Integer> smartList = new SmartList<>(1, 2, 3);
    assertThat(smartList).hasSize(3);
    assertThat(smartList.get(1)).isEqualTo(2);

    smartList.remove(1);
    assertThat(smartList.get(1)).isEqualTo(3);
    assertThat(smartList).hasSize(2);
    assertThrows(IndexOutOfBoundsException.class, () -> smartList.get(2));

    smartList.remove(1);
    assertThat(smartList).hasSize(1);
    assertThrows(IndexOutOfBoundsException.class, () -> smartList.get(1));
    smartList.remove(0);
    assertThat(smartList).isEmpty();
    assertThat(smartList.getInternalData()).isNull();
  }

  @Test
  public void testChangeOriginArray() {
    Integer[] array = new Integer[]{1, 2, 3};

    List<Integer> smartList = new SmartList<>(array);
    assertThat(smartList.get(0)).isEqualTo(1);

    array[0] = 5;
    assertThat(smartList.get(0)).isEqualTo(1);

    smartList.set(1, 10);
    assertThat(smartList.get(1)).isEqualTo(10);
    assertThat(array[1]).isEqualTo(2);
  }
}
