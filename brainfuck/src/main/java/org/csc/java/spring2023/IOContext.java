package org.csc.java.spring2023;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/**
 * Устройство ввода-вывода
 */
public class IOContext {

  protected InputStream in;
  protected OutputStream out;

  public IOContext(InputStream in, OutputStream out) {
    this.in = in;
    this.out = out;
  }

  /**
   * Возвращает байт, считанный из потока {@link IOContext#in}. Блокируется до тех пор, пока байт
   * не станет доступен.
   *
   * @return Один байт, считанный из потока ввода.
   * @throws IllegalStateException если поток был закрыт
   * @throws UncheckedIOException если произошла ошибка при чтении
   */
  public byte readByte() {
    try {
      int resultByte = in.read();

      if (resultByte == -1) {
        throw new IllegalStateException("readByte failed, STDIN has been closed");
      }

      return (byte) resultByte;
    } catch (IOException e) {
      throw new UncheckedIOException("readByte failed ", e);
    }
  }

  public void writeByte(byte b) {
    try {
      out.write(b);
      out.flush();
    } catch (IOException e) {
      throw new UncheckedIOException("writeByte failed", e);
    }
  }
}
