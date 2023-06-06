package org.csc.java.spring2023;

public class MemoryImpl implements Memory {

  private int pos;
  private final byte[] memory;

  public MemoryImpl(int memorySize) {
    memory = new byte[memorySize];
    pos = 0;
  }

  @Override
  public void shiftRight() {
    pos++;
  }

  @Override
  public void shiftLeft() {
    pos--;
  }

  @Override
  public void setByte(byte value) throws MemoryAccessException {
    checkIndex();
    memory[pos] = value;
  }

  @Override
  public byte getByte() throws MemoryAccessException {
    checkIndex();
    return memory[pos];
  }

  private void checkIndex() {
    if (0 > pos || pos >= getSize()) {
      throw new MemoryAccessException(pos);
    }
  }

  @Override
  public int getSize() {
    return memory.length;
  }
}
