package org.csc.java.spring2023.moviedatabase;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * База данных фильмов, хранящая все свои фильмы в массиве.
 */
class ArrayBasedMovieDatabase implements MovieDatabase {

  private final Movie[] movies;

  public ArrayBasedMovieDatabase(Movie... movies) {
    this.movies = movies;
  }

  @Override
  public Stream<Movie> movies() {
    return Arrays.stream(movies);
  }
}
