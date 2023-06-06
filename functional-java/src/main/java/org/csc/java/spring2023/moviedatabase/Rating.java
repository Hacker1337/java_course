package org.csc.java.spring2023.moviedatabase;

/**
 * Value-класс, обозначающий рейтинг фильма.
 * <p>
 * Рейтинг - это число в диапазоне [0, 10].
 */
public record Rating(double value) implements Comparable<Rating> {

  public Rating {
    if (value < 0 || value > 10) {
      throw new IllegalArgumentException(
          "Unacceptable rating value " + value + ", it should be in [0, 10]");
    }

  }

  public static Rating of(double value) {
    return new Rating(value);
  }

  @Override
  public int compareTo(Rating o) {
    return Double.compare(value, o.value);
  }
}