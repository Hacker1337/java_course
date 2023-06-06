package org.csc.java.spring2023.moviedatabase;

import java.util.stream.Stream;

/**
 * Пустая база данных, нужна для тестов.
 */
class EmptyMovieDatabase implements MovieDatabase {
  @Override
  public Stream<Movie> movies() {
    return Stream.empty();
  }
}
