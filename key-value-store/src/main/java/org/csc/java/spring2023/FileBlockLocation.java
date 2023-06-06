package org.csc.java.spring2023;

import java.io.File;
import java.nio.file.Path;

/**
 * Класс-дескриптор блока, в котором хранится значение.
 * <p>
 * Если вам это потребуется, можете заменить этот record на class.
 */
record FileBlockLocation(String fileName, int offset, int size) {

  public File file(Path workDir) {
    return new File(workDir.toFile(), fileName);
  }

}
