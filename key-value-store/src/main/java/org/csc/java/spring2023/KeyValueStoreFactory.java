package org.csc.java.spring2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class KeyValueStoreFactory {

  private KeyValueStoreFactory() {
  }

  public static KeyValueStore create(Path workingDir, int valueFileSize) throws IOException {
    if (valueFileSize <= 0) {
      throw new IllegalArgumentException(
          "maxValueFileSize must be positive, but got " + valueFileSize);
    }
    if (!Files.exists(workingDir)) {
      throw new NoSuchFileException("workingDir must exist");
    }
    if (!Files.isDirectory(workingDir)) {
      throw new IllegalArgumentException("workingDir must be a directory");
    }
    return new KeyValueStoreImpl(workingDir, valueFileSize);
  }
}
