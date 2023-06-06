package org.csc.java.spring2023.moviedatabase;

import java.time.Year;
import java.util.Set;

/**
 * Интерфейс, описывающий кинофильм.
 * <p>
 * Ни один из методов не может вернуть null.
 */
public interface Movie {

  String getTitle();

  Genre getGenre();

  Set<Actor> getCast();

  Year getReleaseYear();

  Rating getRating();
}
