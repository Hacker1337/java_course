package org.csc.java.spring2023;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexManagerImpl implements IndexManager {

  private static final String fileName = "index";
  private Map<ByteWrapper, List<FileBlockLocation>> index;
  private final File indexFile;

  public IndexManagerImpl(Path workDir) throws IOException {
    indexFile = new File(workDir.toString(), fileName);
    if (indexFile.createNewFile()) {
      index = new HashMap<>();
    } else {
      index = ConfigSaver.loadIndex(indexFile);
    }

  }


  /**
   * Создает связь key -> listOf(FileBlockLocation) в индексе.
   */
  @Override
  public void add(byte[] key, List<FileBlockLocation> writtenBlocks) {
    index.put(new ByteWrapper(key), writtenBlocks);
  }

  @Override
  public void remove(byte[] key) {
    index.remove(new ByteWrapper(key));
  }

  /**
   * Возвращает список блоков, в которых хранится значение.
   */
  @Override
  public List<FileBlockLocation> getFileBlocksLocations(byte[] key) {
    if (index == null) {
      throw new IllegalStateException("Storage is closed");
    }
    return index.get(new ByteWrapper(key));
  }

  /**
   * Closes this stream and releases any system resources associated with it. If the stream is
   * already closed then invoking this method has no effect.
   *
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void close() throws IOException {
    if (index == null) {
      return;
    }
    ConfigSaver.saveIndex(index, indexFile);
    index = null;
  }

}
