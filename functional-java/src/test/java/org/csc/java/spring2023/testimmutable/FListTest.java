package org.csc.java.spring2023.testimmutable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.csc.java.spring2023.immutable.FListUtils.concat;
import static org.csc.java.spring2023.immutable.FListUtils.cons;
import static org.csc.java.spring2023.immutable.FListUtils.empty;
import static org.csc.java.spring2023.immutable.FListUtils.filter;
import static org.csc.java.spring2023.immutable.FListUtils.foldl;
import static org.csc.java.spring2023.immutable.FListUtils.foldr;
import static org.csc.java.spring2023.immutable.FListUtils.forEach;
import static org.csc.java.spring2023.immutable.FListUtils.listOf;
import static org.csc.java.spring2023.immutable.FListUtils.map;
import static org.csc.java.spring2023.immutable.FListUtils.toJavaList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.csc.java.spring2023.immutable.FList;
import org.csc.java.spring2023.immutable.FListUtils;
import org.junit.jupiter.api.Test;

/**
 * Этот тест специально находится в пакете, отличном от пакета для решения.
 * <p>
 * Это сделано, чтобы случайно не воспользоваться package-private функциями и классами оттуда.
 */
public class FListTest {

  @Test
  void testListsCreation() {
    FList<Integer> emptyList = empty();

    assertThat(emptyList.size()).isEqualTo(0);
    assertThat(toJavaList(emptyList)).isEmpty();

    FList<Integer> threeElements = cons(1, cons(2, cons(3, emptyList)));

    assertThat(threeElements.size()).isEqualTo(3);
    assertThat(toJavaList(threeElements)).containsExactly(1, 2, 3);

    assertThat(concat(threeElements, threeElements).size()).isEqualTo(6);
    assertThat(toJavaList(concat(threeElements, threeElements))).containsExactly(1, 2, 3, 1, 2, 3);

    FList<String> listOfTwoStrings = listOf("hello", "there");

    assertThat(listOfTwoStrings.size()).isEqualTo(2);
    assertThat(toJavaList(listOfTwoStrings)).containsExactly("hello", "there");
  }

  @Test
  void testCons() {

    FList<Integer> ints = listOf(1);
    FList<Number> numbers = cons(2.0, ints);
    FList<Object> objects = cons("Hello", numbers);

    assertThat(objects.get(0)).isEqualTo("Hello");
    assertThat(objects.get(1)).isEqualTo(2.0);
    assertThat(objects.get(2)).isEqualTo(1);

  }

  @Test
  void testFoldingRight() {
    {
      FList<String> names = listOf("foo", "bar", "baz");

      String foldingResult = foldr(names, "arg", (name, acc) -> name + "(" + acc + ")");

      assertThat(foldingResult)
          .isEqualTo("foo(bar(baz(arg)))");
    }

    {
      FList<Integer> numbers = listOf(10, 20, 30);

      List<String> foldingResult = foldr(
          numbers,
          new ArrayList<>(),
          (number, acc) -> {
            acc.add(number.toString());
            return acc;
          });

      assertThat(foldingResult).containsExactly("30", "20", "10");
    }
  }

  @Test
  void testFoldingLeft() {
    {
      FList<Integer> names = listOf(1, 2, 3);

      String foldingResult = foldl(names, "Start", (acc, number) -> acc + " => " + number);

      assertThat(foldingResult).isEqualTo("Start => 1 => 2 => 3");
    }
  }

  @Test
  void testGetOperation() {
    FList<Integer> list = listOf(1, 2, 3);

    assertThat(list.get(0)).isEqualTo(1);
    assertThat(list.get(1)).isEqualTo(2);
    assertThat(list.get(2)).isEqualTo(3);

    assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
    assertThatThrownBy(() -> list.get(3)).isInstanceOf(IndexOutOfBoundsException.class);

    assertThatThrownBy(() -> empty().get(0)).isInstanceOf(IndexOutOfBoundsException.class);
  }

  @Test
  void testConcat() {
    {
      FList<Integer> threeElements = listOf(1, 2, 3);

      assertThat(concat(threeElements, threeElements).size()).isEqualTo(6);
      assertThat(toJavaList(concat(threeElements, threeElements)))
          .containsExactly(1, 2, 3, 1, 2, 3);
    }

//    throw new CommentedTestPlaceholderException();
//    {
//      FList<Integer> ints = listOf(1, 2, 3);
//      FList<Double> doubles = listOf(4.0, 5.0, 6.0);
//
//      FList<Number> numbers = concat(ints, doubles);
//
//      assertThat(toJavaList(numbers)).containsExactly(1, 2, 3, 4.0, 5.0, 6.0);
//    }
  }

  @Test
  void testEquals() {
    {
      assertThat(empty()).isEqualTo(empty());

      assertThat(listOf(1, 2, 3))
          .withFailMessage("Lists with same content must be equal")
          .isEqualTo(cons(1, cons(2, cons(3, empty()))));

      assertThat(listOf(1, 2, 3)).isNotEqualTo(empty());
      assertThat(listOf(1, 2, 3)).isNotEqualTo(cons(1, cons(2, empty())));

      assertThat(listOf(1, 2, 3)).isNotEqualTo(listOf(1, 2, 3, 4));
    }

    {
      List<Integer> firstJavaList = new ArrayList<>(Arrays.asList(1, 2, 3));
      List<Integer> secondJavaList = new ArrayList<>(Arrays.asList(1, 2, 3));

      FList<List<Integer>> firstList = listOf(firstJavaList);
      FList<List<Integer>> secondList = listOf(secondJavaList);

      assertThat(firstList.size()).isEqualTo(1);
      assertThat(secondList.size()).isEqualTo(1);

      assertThat(firstList).isEqualTo(secondList);

      firstJavaList.add(4);

      assertThat(firstList)
          .withFailMessage(
              "firstJavaList and secondJavaList are not equal => firstList and secondList should not be equal")
          .isNotEqualTo(secondList);

      secondJavaList.add(4);

      assertThat(firstList)
          .withFailMessage(
              "firstJavaList and secondJavaList are equal => firstList and secondList should be equal")
          .isEqualTo(secondList);
    }
  }

  @Test
  void testToString() {
    assertThat(empty()).hasToString("FList []");

    assertThat(listOf(1, 2, 3)).hasToString("FList [1, 2, 3]");

    assertThat(listOf(listOf(1, 2, 3), listOf(4, 5, 6), empty()))
        .hasToString("FList [FList [1, 2, 3], FList [4, 5, 6], FList []]");
  }

  @Test
  void testHashCode() {
    {
      assertThat(listOf(1, 2, 3)).hasSameHashCodeAs(listOf(1, 2, 3));
      assertThat(listOf(1, 2, 3)).doesNotHaveSameHashCodeAs(listOf(1, 2));
    }

    {
      class CustomHashCode {

        private final int value;

        CustomHashCode(int value) {
          this.value = value;
        }

        @Override
        public int hashCode() {
          return value;
        }
      }

      CustomHashCode firstValue = new CustomHashCode(42);
      CustomHashCode secondValue = new CustomHashCode(43);

      assertThat(listOf(firstValue)).hasSameHashCodeAs(listOf(firstValue));
      assertThat(listOf(secondValue)).hasSameHashCodeAs(listOf(secondValue));

      assertThat(listOf(firstValue, secondValue))
          .hasSameHashCodeAs(listOf(firstValue, secondValue));

      assertThat(listOf(firstValue, secondValue, firstValue))
          .withFailMessage(
              "Lists have different elements, so they should probably have different hashes")
          .doesNotHaveSameHashCodeAs(listOf(secondValue, firstValue, secondValue));

      assertThat(listOf(firstValue)).doesNotHaveSameHashCodeAs(listOf(secondValue));
    }
  }

  @Test
  void testForEach() {
    {
      StringBuilder result = new StringBuilder();

      forEach(listOf("Hello", " ", "there", "!"), result::append);

      assertThat(result.toString()).isEqualTo("Hello there!");
    }

    {
      StringBuilder result = new StringBuilder();
      Consumer<Object> consumer = result::append;

      FList<String> list = listOf("Hello", " ", "there", "!");

      FListUtils.<CharSequence>forEach(list, consumer);

      assertThat(result.toString()).isEqualTo("Hello there!");
    }
  }

  @Test
  void testMap() {
    {
      assertThat(map(listOf("1", "2", "3"), i -> i))
          .isEqualTo(listOf("1", "2", "3"));

      assertThat(map(listOf(1, 2, 3), Object::toString))
          .isEqualTo(listOf("1", "2", "3"));
    }

    {
      FList<Integer> originalList = listOf(1, 2, 3);
      Function<Object, String> mapper = Object::toString;

      FList<CharSequence> resultList = FListUtils.<Number, CharSequence>map(originalList, mapper);

      assertThat(resultList)
          .isEqualTo(listOf("1", "2", "3"));
    }
  }

  @Test
  void testFilter() {
    {
      FList<Integer> originalList = listOf(1, 2, 3, 4, 5);

      FList<Integer> evenNumbers = filter(originalList, i -> i % 2 == 0);
      FList<Integer> oddNumbers = filter(originalList, i -> i % 2 != 0);

      assertThat(evenNumbers).isEqualTo(listOf(2, 4));
      assertThat(oddNumbers).isEqualTo(listOf(1, 3, 5));
    }

    {
      FList<Integer> list = listOf(1, 2, 3, 100);

      Predicate<Object> lengthPredicate = number -> number.toString().length() < 3;

      assertThat(FListUtils.<Number>filter(list, lengthPredicate)).isEqualTo(listOf(1, 2, 3));
    }
  }
}
