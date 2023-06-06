package org.csc.java.spring2023.bidimap;

import java.util.List;
import java.util.Map;

/**
 * Коллекция, хранящая пары ключ + значение. Представляет собой двунаправленное отображение.
 * <p>
 * Позволяет получить значение по ключу с помощью {@link #get(K)}, а также множество ключей с
 * указанным значением с помощью {@link #getKeysByValue(V)}.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public interface BidiMap<K, V> extends Map<K, V> {

  /**
   * Получить все ключи, которым соответствует переданное значение.
   *
   * @param value значение
   * @return список ключей или null (в случае, если ключей с таким значением нет)
   */
  List<K> getKeysByValue(V value);

  /**
   * Удаляет все пары, у которых значение равно переданному.
   * <p>
   * Проверка на равенство должна производиться через вызов метода {@link Object#equals(Object)}
   *
   * @param value значение
   * @return список ключей, которые были удалены (или null, если ничего не было удалено)
   */
  List<K> removeByValue(V value);
}
