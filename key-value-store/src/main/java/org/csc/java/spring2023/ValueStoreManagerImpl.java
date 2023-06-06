package org.csc.java.spring2023;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.file.StandardOpenOption.READ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.UUID;

class ValueStoreManagerImpl implements ValueStoreManager {

  private final Path workDir;
  private final int fileSize;
  private final File emptyBlocksFile;
  private Deque<FileBlockLocation> emptyBlocks;

  ValueStoreManagerImpl(Path workDir, int fileSize) throws IOException {
    this.workDir = workDir;
    this.fileSize = fileSize;
    this.emptyBlocksFile = new File(this.workDir.toFile(), "emptyBlocks");

    if (emptyBlocksFile.createNewFile()) {
      this.emptyBlocks = new ArrayDeque<>();
    } else {
      this.emptyBlocks = ConfigSaver.loadEmptyBlocks(emptyBlocksFile);
    }


  }

  /**
   * Записывает значение в файл, возвращает блоки, в которые было записано значение для добавления
   * этой информации в индекс.
   */
  @Override
  public List<FileBlockLocation> add(byte[] value) throws IOException {
    if (emptyBlocks == null) {
      throw new IllegalStateException("Storage is closed");
    }
    List<FileBlockLocation> usedBlocks = new ArrayList<>();
    int offset = 0;
    int restSize = value.length - offset;
    while (restSize > 0) {
      FileBlockLocation block;
      if (emptyBlocks.isEmpty()) {
        block = addFile();
      } else {
        block = emptyBlocks.pop();
      }
      writeToFile(value, offset, Math.min(restSize, block.size()), block.file(workDir),
          block.offset());

      if (block.size() > restSize) {
        // use part of the block
        emptyBlocks.add(new FileBlockLocation(block.fileName(), block.offset() + restSize,
            block.size() - restSize));
        usedBlocks.add(new FileBlockLocation(block.fileName(), block.offset(), restSize));
      } else {
        usedBlocks.add(block);
      }
      offset += Math.min(block.size(), restSize);

      restSize = value.length - offset;
    }
    return usedBlocks;
  }

  private void writeToFile(byte[] value, int offset, int size, File file, int fileOffset)
      throws IOException {
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
      randomAccessFile.seek(fileOffset);
      randomAccessFile.write(value, offset, size);
    }
  }

  /**
   * Возвращает входной поток из которого можно читать значение из конкретного блока.
   */
  @Override
  public InputStream openBlockStream(FileBlockLocation block) throws IOException {
    try (FileChannel channel = FileChannel.open(Paths.get(block.file(workDir).getAbsolutePath()),
        READ)) {
      MappedByteBuffer content = channel.map(READ_ONLY, block.offset(), block.size());
      return new ByteBufferBackedInputStream(content);
    }

  }

  private static class ByteBufferBackedInputStream extends InputStream {

    private final ByteBuffer buf;

    public ByteBufferBackedInputStream(ByteBuffer buf) {
      this.buf = buf;
    }

    public int read() {
      if (!buf.hasRemaining()) {
        return -1;
      }
      return buf.get() & 0xFF;
    }

    public int read(byte[] bytes, int off, int desiredLen) {
      if (!buf.hasRemaining()) {
        return -1;
      }

      int len = Math.min(desiredLen, buf.remaining());
      buf.get(bytes, off, len);
      return len;
    }
  }

  /**
   * Добавляет удаленные блоки в список свободных блоков.
   */
  @Override
  public void remove(List<FileBlockLocation> valueBlocksLocations) {
    emptyBlocks.addAll(valueBlocksLocations);
  }

  /**
   * Closes this stream and releases any system resources associated with it. If the stream is
   * already closed then invoking this method has no effect.
   *
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void close() throws IOException {
    if (emptyBlocks == null) {
      return;
    }
    ConfigSaver.saveEmptyBlocks(emptyBlocks, emptyBlocksFile);
    emptyBlocks = null;
  }

  /**
   * Creates new file for storing values.
   *
   * @return location of the new block.
   */
  private FileBlockLocation addFile() throws IOException {
    String fileName = UUID.randomUUID().toString();
    FileBlockLocation block = new FileBlockLocation(fileName, 0, fileSize);
    File filePath = block.file(workDir);
    try (RandomAccessFile file = new RandomAccessFile(filePath, "rw")) {
      file.setLength(fileSize);
    }
    return block;
  }
}
