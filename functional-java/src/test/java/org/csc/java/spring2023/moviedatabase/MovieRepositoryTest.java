package org.csc.java.spring2023.moviedatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Year;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MovieRepositoryTest {

  public static final MovieRepository EMPTY_REPOSITORY =
      new MovieRepository(new EmptyMovieDatabase());

  @Test
  void testCollectTopRatedMoviesForAllTimeRequirements() {
    MovieRepository repository = EMPTY_REPOSITORY;

    assertThatThrownBy(() -> repository.collectTopRatedMoviesForAllTime(0))
        .withFailMessage("Limit <= 0 должен быть запрещен")
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> repository.collectTopRatedMoviesForAllTime(-1))
        .withFailMessage("Limit <= 0 должен быть запрещен")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testCollectTopRatedMoviesForAllTime() {
    {
      assertThat(EMPTY_REPOSITORY.collectTopRatedMoviesForAllTime(10)).isEmpty();
    }

    {
      MovieRepository repository = new MovieRepository(databaseWithMovies(
          MockMovie.titled("First").withRating(3),
          MockMovie.titled("Second").withRating(5),
          MockMovie.titled("Third").withRating(2),
          MockMovie.titled("Four").withRating(9.5),
          MockMovie.titled("Fifth").withRating(9.5)
      ));

      List<Movie> topMovies = repository.collectTopRatedMoviesForAllTime(3);

      assertThat(topMovies)
          .map(Movie::getTitle)
          .containsExactly("Fifth", "Four", "Second");
    }

    {
      MovieRepository repository = new MovieRepository(databaseWithMovies(
          MockMovie.titled("First").withRating(3),
          MockMovie.titled("Second").withRating(5)
      ));

      List<Movie> topMovies = repository.collectTopRatedMoviesForAllTime(100);

      assertThat(topMovies)
          .map(Movie::getTitle)
          .containsExactly("Second", "First");
    }
  }

  @Test
  void testCollectTopRatedMovieByActor() {
    {
      assertThat(EMPTY_REPOSITORY.collectTopRatedMovieByActor()).isEmpty();
    }

    {
      MockActor firstActor = new MockActor("John", "Actor1");
      MockActor secondActor = new MockActor("John", "Actor2");
      MockActor thirdActor = new MockActor("John", "Actor3");
      MockActor noName = new MockActor("John", "NoName");

      MovieRepository repository = new MovieRepository(databaseWithMovies(
          MockMovie.titled("Best for Actor1")
              .withCast(firstActor, secondActor)
              .withRating(8),

          MockMovie.titled("Some B movie with bad rating")
              .withCast(firstActor, secondActor, thirdActor)
              .withRating(4),

          MockMovie.titled("ZZZ Second best for Actor2").withCast(secondActor).withRating(9.5),

          MockMovie.titled("Best for Actor2").withCast(secondActor, thirdActor).withRating(9.5),

          MockMovie.titled("Best for Actor3").withCast(thirdActor).withRating(9.6)
      ));

      Map<Actor, Movie> bestMovieByActor = repository.collectTopRatedMovieByActor();

      assertThat(bestMovieByActor).hasSize(3);
      assertThat(bestMovieByActor).doesNotContainKey(noName);

      Movie bestFirstActorMovie = bestMovieByActor.get(firstActor);
      Movie bestSecondActorMovie = bestMovieByActor.get(secondActor);
      Movie bestThirdActorMovie = bestMovieByActor.get(thirdActor);

      assertThat(bestFirstActorMovie.getTitle()).isEqualTo("Best for Actor1");
      assertThat(bestSecondActorMovie.getTitle()).isEqualTo("Best for Actor2");
      assertThat(bestThirdActorMovie.getTitle()).isEqualTo("Best for Actor3");
    }
  }

  @Test
  void testCollectTopRatedMoviesByYearsAssertions() {

    assertThatThrownBy(() -> EMPTY_REPOSITORY.collectTopRatedMoviesByYears(0))
        .withFailMessage("Limit <= 0 должен быть запрещен")
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> EMPTY_REPOSITORY.collectTopRatedMoviesByYears(-1))
        .withFailMessage("Limit <= 0 должен быть запрещен")
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testCollectTopRatedMoviesByYears() {
    {

      assertThat(EMPTY_REPOSITORY.collectTopRatedMoviesByYears(3)).isEmpty();
    }

    {
      MovieRepository repository = new MovieRepository(databaseWithMovies(
          MockMovie.titled("Top1 in 1995").releasedIn(1995).withRating(9.5),
          MockMovie.titled("ZZZ Top3 in 1995").releasedIn(1995).withRating(9.3),
          MockMovie.titled("Top2 in 1995").releasedIn(1995).withRating(9.3),

          MockMovie.titled("Top4 in 2005").releasedIn(2005).withRating(3.3),
          MockMovie.titled("Top1 in 2005").releasedIn(2005).withRating(5.6),
          MockMovie.titled("Top2 in 2005").releasedIn(2005).withRating(5.55),
          MockMovie.titled("Top3 in 2005").releasedIn(2005).withRating(3.4),

          MockMovie.titled("Top2 in 1980").releasedIn(1980).withRating(9),
          MockMovie.titled("Top1 in 1980").releasedIn(1980).withRating(9.3)
      ));

      Map<Year, List<Movie>> topMoviesByYears = repository.collectTopRatedMoviesByYears(3);

      assertThat(topMoviesByYears).hasSize(3);

      List<Movie> bestMovies1980 = topMoviesByYears.get(Year.of(1980));
      List<Movie> bestMovies1995 = topMoviesByYears.get(Year.of(1995));
      List<Movie> bestMovies2005 = topMoviesByYears.get(Year.of(2005));

      assertThat(bestMovies1980).map(Movie::getTitle)
          .containsExactly("Top1 in 1980", "Top2 in 1980");

      assertThat(bestMovies1995).map(Movie::getTitle)
          .containsExactly("Top1 in 1995", "Top2 in 1995", "ZZZ Top3 in 1995");

      assertThat(bestMovies2005).map(Movie::getTitle)
          .containsExactly("Top1 in 2005", "Top2 in 2005", "Top3 in 2005");
    }
  }

  @Test
  void testCollectingActorsOfGenreRequirements() {
    MovieRepository repository = EMPTY_REPOSITORY;

    assertThatThrownBy(
        () -> repository.collectActorsOfGenreForPeriod(null, Year.of(2000), Year.of(2001)))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(
        () -> repository.collectActorsOfGenreForPeriod(Genre.COMEDY, null, Year.of(2001)))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(
        () -> repository.collectActorsOfGenreForPeriod(Genre.COMEDY, Year.of(2001), null))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(
        () -> repository.collectActorsOfGenreForPeriod(Genre.COMEDY, Year.of(2000), Year.of(2000)))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(
        () -> repository.collectActorsOfGenreForPeriod(Genre.COMEDY, Year.of(2000), Year.of(1999)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void testCollectingActorsOfGenre() {
    {
      assertThat(EMPTY_REPOSITORY.collectActorsOfGenreForPeriod(
          Genre.THRILLER,
          Year.of(2000),
          Year.of(2001))
      ).isEmpty();
    }

    {
      MockActor firstComedyActor = new MockActor("Brian", "Comedy");
      MockActor secondComedyActor = new MockActor("John", "Comedy");
      MockActor thirdComedyActor = new MockActor("Alex", "OtherComedy");

      MockActor thrillerActor = new MockActor("Ben", "Thrill");

      MovieRepository repository = new MovieRepository(databaseWithMovies(
          MockMovie.titled("First Comedy").ofGenre(Genre.COMEDY).releasedIn(1995)
              .withCast(secondComedyActor),
          MockMovie.titled("Second Comedy").ofGenre(Genre.COMEDY).releasedIn(2000)
              .withCast(firstComedyActor),

          MockMovie.titled("Comedy out of period").ofGenre(Genre.COMEDY).releasedIn(2001)
              .withCast(thirdComedyActor),

          MockMovie.titled("Some Thriller").ofGenre(Genre.THRILLER).releasedIn(1996)
              .withCast(thrillerActor)
      ));

      List<Actor> comedyActors = repository.collectActorsOfGenreForPeriod(
          Genre.COMEDY,
          Year.of(1995),
          Year.of(2001)
      );

      assertThat(comedyActors).containsExactly(firstComedyActor, secondComedyActor);
    }

    {
      MockActor singleComedyActor = new MockActor("Brian", "Comedy");

      MovieRepository repository = new MovieRepository(databaseWithMovies(
          MockMovie.titled("First Comedy").ofGenre(Genre.COMEDY).releasedIn(1995)
              .withCast(singleComedyActor),
          MockMovie.titled("Second Comedy").ofGenre(Genre.COMEDY).releasedIn(2000)
              .withCast(singleComedyActor)
      ));

      List<Actor> comedyActors = repository.collectActorsOfGenreForPeriod(
          Genre.COMEDY,
          Year.of(1995),
          Year.of(2001)
      );

      assertThat(comedyActors).containsExactly(singleComedyActor);
    }
  }

  private MovieDatabase databaseWithMovies(Movie... movies) {
    return new ArrayBasedMovieDatabase(movies);
  }

}