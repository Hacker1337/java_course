package org.csc.java.spring2023;

public class MemoryAccessException extends RuntimeException {

  public MemoryAccessException(int pointer) {
    super("Trying to access memory at: " + pointer);
  }
}
