package org.csc.java.spring2023.moviedatabase;

/**
 * Value-класс для описания жанра кинофильма. Для сравнения двух жанров нужно использовать
 * `equals`.
 */
public record Genre(String name) {

  // Несколько часто используемых жанров. Потенциально их может быть сколь угодно много
  public static final Genre COMEDY = new Genre("Comedy");
  public static final Genre THRILLER = new Genre("Thriller");
  public static final Genre DRAMA = new Genre("Drama");
}
