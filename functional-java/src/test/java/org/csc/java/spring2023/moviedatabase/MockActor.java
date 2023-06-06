package org.csc.java.spring2023.moviedatabase;

import java.util.Objects;

/**
 * Простейшая реализация интерфейста актера.
 */
class MockActor implements Actor {

  private final String firstName;
  private final String lastName;

  public MockActor(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MockActor mockActor = (MockActor) o;
    return firstName.equals(mockActor.firstName) && lastName.equals(mockActor.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName);
  }
}
