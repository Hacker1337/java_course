package org.csc.java.spring2023.moviedatabase;

import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link Movie}, специально сделанная для удобного написания тестов.
 */
class MockMovie implements Movie {

  private static final Rating DEFAULT_RATING = Rating.of(5.0);
  private static final Genre DEFAULT_GENRE = Genre.COMEDY;
  private static final Year DEFAULT_RELEASE_YEAR = Year.of(1999);

  private final String title;
  private Genre genre = DEFAULT_GENRE;
  private Year releaseYear = DEFAULT_RELEASE_YEAR;
  private Set<Actor> cast = Collections.emptySet();
  private Rating rating = DEFAULT_RATING;

  MockMovie(String title) {
    this.title = title;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public Genre getGenre() {
    return genre;
  }

  @Override
  public Set<Actor> getCast() {
    return cast;
  }

  @Override
  public Year getReleaseYear() {
    return releaseYear;
  }

  @Override
  public Rating getRating() {
    return rating;
  }

  /**
   * Мы специально копируем каждый Genre заново, чтобы проверить, что equals и hashCode
   * корректно реализованы и используются.
   */
  public MockMovie ofGenre(Genre genre) {
    this.genre = new Genre(genre.name());
    return this;
  }

  public MockMovie releasedIn(int year) {
    this.releaseYear = Year.of(year);
    return this;
  }

  /**
   * Мы специально копируем каждого актера заново, чтобы проверить, что equals и hashCode
   * корректно реализованы и используются.
   */
  public MockMovie withCast(MockActor... actors) {
    this.cast = Arrays.stream(actors)
        .map(actor -> new MockActor(actor.getFirstName(), actor.getLastName()))
        .collect(Collectors.toSet());

    return this;
  }

  public MockMovie withRating(double rating) {
    this.rating = Rating.of(rating);
    return this;
  }

  public static MockMovie titled(String title) {
    return new MockMovie(title);
  }
}