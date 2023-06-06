package org.csc.java.spring2023.bidimap;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.csc.java.spring2023.NotImplementedException;

/**
 * Реализация {@link BidiMap}, основанная на хэш-таблицах ({@link java.util.HashMap}). Вы можете и
 * должны использовать {@link java.util.HashMap} в качестве внутренней структуры данных для
 * реализации {@link HashBidiMap}.
 * <p>
 * {@link AbstractMap} является базовым классом для реализации.
 * <p>
 * В результате реализации методов, представленных в этом классе вы получите полностью
 * работоспособный ассоциативный массив (map).
 * <p>
 * <b>ВАЖНО</b> У данного задания есть два пути выполнения: с реализацией {@link
 * HashBidiMapEntrySet.EntryIterator#remove()} для итератора в {@link HashBidiMapEntrySet} за полный
 * балл, и с реализацией {@link HashBidiMap#remove(Object)} за -1 балл. Подробнее это описано в
 * документации для {@link HashBidiMapEntrySet}.
 * <p>
 * Цена: 4 балла
 */
public class HashBidiMap<K, V> extends AbstractMap<K, V> implements BidiMap<K, V> {

  private final Map<K, V> keyValueStorage;
  private final Map<V, List<K>> valueKeyStorage;

  /**
   * Конструктор. Создаёт пустой {@link HashBidiMap}.
   */
  public HashBidiMap() {
    keyValueStorage = new HashMap<>();
    valueKeyStorage = new HashMap<>();
  }

  /**
   * Конструктор. Создаёт {@link HashBidiMap}, заполненный данными из переданного Map.
   *
   * @param originMap Map
   */
  public HashBidiMap(Map<K, V> originMap) {
    keyValueStorage = new HashMap<>(originMap);
    valueKeyStorage = new HashMap<>();
    for (var entry : originMap.entrySet()) {
      addValue(entry.getValue(), entry.getKey());
    }
  }

  public static <K, V> void removeValue(K key, V oldValue, Map<V, List<K>> valueKeyStorage) {
    List<K> list = valueKeyStorage.get(oldValue);
    list.remove(key);
    if (list.isEmpty()) {
      valueKeyStorage.remove(oldValue);
    }
  }

  private void addValue(V value, K key) {
    if (!valueKeyStorage.containsKey(value)) {
      valueKeyStorage.put(value, new ArrayList<>());
      valueKeyStorage.get(value).add(key);
      return;
    }
    List<K> list = valueKeyStorage.get(value);
    if (!list.contains(key)) {
      list.add(key);
    }


  }

  /**
   * См. комментарий у {@link HashBidiMapEntrySet}.
   *
   * @return множество (set) пар ключ-значение
   */
  @Override
  public Set<Entry<K, V>> entrySet() {
    return new HashBidiMapEntrySet<>(keyValueStorage, valueKeyStorage);
  }

  /**
   * Получить все ключи, которым соответствует переданное значение.
   * <p>
   * <b>Важно:</b> асимптотика метода должна быть О(1)
   *
   * @param value значение
   * @return список ключей или null (в случае, если ключей с таким значением нет)
   */
  @Override
  public List<K> getKeysByValue(V value) {
    if (valueKeyStorage.containsKey(value)) {
      return Collections.unmodifiableList(valueKeyStorage.get(value));
    }
    return null;
  }

  /**
   * Проверяет, присутствует ли хотя бы один ключ, ассоциированный с переданным значением.
   * <p>
   * <b>Важно:</b> асимптотика метода должна быть О(1)
   *
   * @param value значение
   * @return true, если присутствует
   */
  @Override
  public boolean containsValue(Object value) {
    return valueKeyStorage.containsKey(value);
  }

  /*
   * Этот метод переопределен явно на случай, если вы решите не реализовывать
   * {@link HashBidiMapEntrySet.EntryIterator#remove()} - в таком случае вам необходимо будет
   * самостоятельно реализовать данный метод.
   * <p>
   * Если вы реализовали {@link HashBidiMapEntrySet.EntryIterator#remove()}, то данный метод можно
   * удалить - имплементации из {@link AbstractMap} будет достаточно.
   */

  /**
   * Удаляет все пары, у которых значение равно переданному.
   * <p>
   * Проверка на равенство должна производиться через вызов метода {@link Object#equals(Object)}
   *
   * @param value значение
   * @return список ключей, которые были удалены (или null, если ничего не было удалено)
   */
  @Override
  public List<K> removeByValue(V value) {
    List<K> keyList = valueKeyStorage.remove(value);
    if (keyList != null) {
      for (var key : keyList) {
        keyValueStorage.remove(key);
      }
      return Collections.unmodifiableList(keyList);
    }
    return null;
  }

  /**
   * Создаёт двустороннюю ассоциацию между переданными ключом и значением.
   *
   * @param key   ключ
   * @param value значение
   * @return значение, которое ранее хранилось по переданному ключу
   */
  @Override
  public V put(K key, V value) {
    if (keyValueStorage.containsKey(key)) {
      V oldValue = keyValueStorage.get(key);

      removeValue(key, oldValue, valueKeyStorage);
    }
    addValue(value, key);
    return keyValueStorage.put(key, value);
  }

  /**
   * Реализуйте недостающие методы {@link HashBidiMapEntrySet} и воспользуйтесь им в методе
   * {@link HashBidiMap#entrySet()}. Возвращаемое множество должно поддерживать удаление пар при
   * обходе итератором (метод {@link HashBidiMapEntrySet.EntryIterator#remove()}).
   * <p>
   * N.B. Данный итератор используется при удалении элементов через {@link BidiMap#remove(K)}.
   * <p>
   * В качестве альтернативы вы можете не реализовывать удаление в этом множестве, а реализовать
   * метод {@link HashBidiMap#remove(Object)} (-1 балл)
   */
  private static class HashBidiMapEntrySet<K, V> extends AbstractSet<Entry<K, V>> {

    Map<K, V> keyToValue;
    Map<V, List<K>> valueToKeys;

    public HashBidiMapEntrySet(Map<K, V> keyToValue, Map<V, List<K>> valueToKeys) {
      this.keyToValue = keyToValue;
      this.valueToKeys = valueToKeys;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
      return new EntryIterator(this);
    }

    @Override
    public int size() {
      return this.keyToValue.size();
    }

    private class EntryIterator implements Iterator<Entry<K, V>> {

      private final Iterator<Entry<K, V>> kvIterator;
      private Entry<K, V> lastEntry;

      EntryIterator(HashBidiMapEntrySet<K, V> entrySet) {
        lastEntry = null;
        kvIterator = entrySet.keyToValue.entrySet().iterator();
      }

      @Override
      public boolean hasNext() {
        return kvIterator.hasNext();
      }

      @Override
      public Entry<K, V> next() {
        lastEntry = kvIterator.next();
        return lastEntry;
      }

      /**
       * Если вы решили не реализовывать этот метод, то не удаляйте выбрасывание
       * {@link NotImplementedException} из него - на это полагаются тесты.
       */
      @Override
      public void remove() {
        if (this.lastEntry == null) {
          throw new IllegalStateException();
        }
        kvIterator.remove();
        removeValue(lastEntry.getKey(), lastEntry.getValue(), valueToKeys);
        lastEntry = null;
      }


    }
  }
}


