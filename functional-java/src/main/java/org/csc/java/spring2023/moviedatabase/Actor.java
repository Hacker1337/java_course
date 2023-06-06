package org.csc.java.spring2023.moviedatabase;

/**
 * Интерфейс, описывающий актера.
 * <p>
 * Класс, который имплементирует этот интерфейс, обязан реализовать hashCode и equals.
 * <p>
 * Ни один из методов не может вернуть null.
 */
public interface Actor {

  String getFirstName();

  String getLastName();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}

