package org.csc.java.spring2023;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class KeyValueStoreImpl implements KeyValueStore {

  private final IndexManager indexManager;
  private final ValueStoreManager valueStoreManager;

  KeyValueStoreImpl(Path workDir, int fileSize) throws IOException {
    indexManager = new IndexManagerImpl(workDir);
    valueStoreManager = new ValueStoreManagerImpl(workDir, fileSize);
  }

  /**
   * Проверяет, есть ли такой ключ в хранилище.
   */
  @Override
  public boolean contains(byte[] key) throws IOException {
    Objects.requireNonNull(key, "Key can't be null");
    return indexManager.getFileBlocksLocations(key) != null;
  }

  /**
   * По ключу возвращает входной поток из которого можно (лениво) читать значение.
   */
  @Override
  public InputStream openValueStream(byte[] key) throws IOException {
    Objects.requireNonNull(key, "Key can't be null");
    var blocks = indexManager.getFileBlocksLocations(key);
    if (blocks == null) {
      throw new IOException("Key: " + new String(key) + " not found");
    }
    List<InputStream> streams = new ArrayList<>(blocks.size());
    for (FileBlockLocation block : blocks) {
      streams.add(valueStoreManager.openBlockStream(block));
    }
    return new SequenceInputStream(Collections.enumeration(streams));
  }

  /**
   * Полностью считывает значение в массив байтов и возвращает его.
   */
  @Override
  public byte[] loadValue(byte[] key) throws IOException {
    Objects.requireNonNull(key, "Key can't be null");
    try (var stream = openValueStream(key)) {
      return stream.readAllBytes();
    }
  }

  /**
   * Записывает новое значение по ключу. Если ключ уже существует в базе, тогда перезаписывает
   * старое значение.
   */
  @Override
  public void upsert(byte[] key, byte[] value) throws IOException {
    Objects.requireNonNull(key, "Key can't be null");
    remove(key);
    indexManager.add(key, valueStoreManager.add(value));
  }

  /**
   * Удаляет значение из базы. Если значение существовало, то возвращает true, иначе false.
   */
  @Override
  public boolean remove(byte[] key) throws IOException {
    Objects.requireNonNull(key, "Key can't be null");
    var blocks = indexManager.getFileBlocksLocations(key);
    if (blocks == null) {
      return false;
    }
    valueStoreManager.remove(blocks);
    indexManager.remove(key);
    return true;
  }

  /**
   * TestOnly Возвращает IndexManager, соответствующий текущему хранилищу.
   */
  @Override
  public IndexManager getIndexManager() {
    return indexManager;
  }

  /**
   * Closes this stream and releases any system resources associated with it. If the stream is
   * already closed then invoking this method has no effect.
   *
   * @throws IOException if an I/O error occurs
   */
  @Override
  public void close() throws IOException {
    indexManager.close();
    valueStoreManager.close();
  }
}
