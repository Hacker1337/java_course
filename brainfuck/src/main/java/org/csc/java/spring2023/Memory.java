package org.csc.java.spring2023;

/**
 * Реализующий класс должен полностью абстрагировать пользователя от работы с массивом байтов
 */
public interface Memory {

  /**
   * Двигает указатель на следующую ячейку
   */
  void shiftRight();

  /**
   * Двигает указатель на предыдущую ячейку
   */
  void shiftLeft();

  /**
   * Устанавливает значение в текущей ячейке
   *
   * @param value значение в текущей ячейке
   */
  void setByte(byte value) throws MemoryAccessException;

  /**
   * @return значение в текущей ячейке
   */
  byte getByte() throws MemoryAccessException;

  /**
   * @return размер памяти
   */
  int getSize();
}
