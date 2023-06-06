package org.csc.java.spring2023;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ConfigSaver {

  /**
   * Static class for saving KeyValueStore and ValueStoreManage state on disk and loading them from
   * disk.
   */
  private ConfigSaver() {
  }


  static void saveEmptyBlocks(Deque<FileBlockLocation> emptyBlocks,
      File saveFile) throws IOException {
    try (DataOutputStream out = new DataOutputStream(new FileOutputStream(saveFile))) {
      writeBlocks(out, emptyBlocks);
    }
  }

  static Deque<FileBlockLocation> loadEmptyBlocks(File loadFile) throws IOException {
    Deque<FileBlockLocation> deque;
    try (DataInputStream in = new DataInputStream(new FileInputStream(loadFile))) {
      deque = new ArrayDeque<>(readBlocks(in));
    }
    return deque;
  }

  /**
   * Сохраняет index маппу в формате int количество ключей для каждого ключа { int размер ключа,
   * столько байт -- ключ, int -- количество блоков для каждого блока { int -- размер названия
   * файла, столько байт - название файла int offset, int size } }.
   */
  static void saveIndex(Map<ByteWrapper, List<FileBlockLocation>> map, File indexFile)
      throws IOException {
    try (DataOutputStream out = new DataOutputStream(
        Files.newOutputStream(indexFile.toPath()))) {
      out.writeInt(map.size());
      for (var entry : map.entrySet()) {
        byte[] bytes = entry.getKey().getBytes();
        List<FileBlockLocation> fileBlockLocations = entry.getValue();
        writeBytes(out, bytes);
        writeBlocks(out, fileBlockLocations);
      }
    }
  }

  static Map<ByteWrapper, List<FileBlockLocation>> loadIndex(File indexFile) throws IOException {
    Map<ByteWrapper, List<FileBlockLocation>> map;
    try (DataInputStream in = new DataInputStream(Files.newInputStream(indexFile.toPath()))) {
      int entryNumber = in.readInt();
      // capacity to avoid rehashing in the beginning
      map = new HashMap<>((int) Math.ceil(entryNumber / 0.75), 0.75f);
      for (int i = 0; i < entryNumber; i++) {
        byte[] key = readBytes(in);
        List<FileBlockLocation> blockList = readBlocks(in);
        map.put(new ByteWrapper(key), blockList);
      }
    }
    return map;
  }

  private static void writeBytes(DataOutputStream out, byte[] bytes) throws IOException {
    out.writeInt(bytes.length);
    out.write(bytes);
  }

  private static byte[] readBytes(DataInputStream in) throws IOException {
    int bytesSize = in.readInt();
    return in.readNBytes(bytesSize);
  }

  private static void writeBlocks(DataOutputStream out, Collection<FileBlockLocation> emptyBlocks)
      throws IOException {
    out.writeInt(emptyBlocks.size());
    for (FileBlockLocation block : emptyBlocks) {
      out.writeUTF(block.fileName());
      out.writeInt(block.offset());
      out.writeInt(block.size());
    }
  }

  private static List<FileBlockLocation> readBlocks(DataInputStream in)
      throws IOException {
    int blocksNumber = in.readInt();
    List<FileBlockLocation> blocks = new ArrayList<>(blocksNumber);
    for (int j = 0; j < blocksNumber; j++) {
      String fileName = in.readUTF();
      int offset = in.readInt();
      int size = in.readInt();
      blocks.add(new FileBlockLocation(fileName, offset, size));
    }
    return blocks;
  }
}
