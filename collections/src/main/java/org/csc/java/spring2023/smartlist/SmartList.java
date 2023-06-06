package org.csc.java.spring2023.smartlist;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * Список, оптимизированный по памяти для хранения 0 или 1 элементов.
 * <p>
 * {@link AbstractList} является базовым классом для реализации списков. В результате реализации
 * методов, представленных в данном классе, вы получите полностью работоспособный список.
 * <p>
 * Ограничения:
 * <ul>
 * <li>У класса должно быть <b>не больше 2</b> полей (но при необходимости вы можете добавлять статические поля). Предполагается, что это будет поле для хранения размера и для хранения элементов списка.</li>
 * <li>В реализации данного класса <b>категорически запрещено</b> использовать существующие коллекции или списки.</li>
 * <li>При размере 0 список не хранит ничего, кроме размера. Это значит, что список не ссылается ни на какие объекты или массивы объектов.</li>
 * <li>При размере 1 список хранит единственный элемент, т.е. хранит прямую ссылку на этот элемент.</li>
 * <li>При размере больше 1 список хранит ссылку на массив, содержащий элементы списка (аналогично тому, как это сделано в {@link java.util.ArrayList}</li>
 * </ul>
 * Не забудьте, что вам нужно вернуть значение поля, используемого для хранения элементов, из метода
 * {@link SmartList#getInternalData}.
 * <p>
 * Цена: 4 балла
 *
 * @param <V> тип элементов в списке
 */
public class SmartList<V> extends AbstractList<V> implements RandomAccess {

  Object data;
  int size;

  /**
   * Конструктор. Создаёт пустой список.
   */
  public SmartList() {
    size = 0;
  }

  /**
   * Конструктор. Создаёт список, состоящий из элементов переданной коллекции.
   *
   * @param collection коллекция
   */
  public SmartList(Collection<? extends V> collection) {
    size = collection.size();
    data = new Object[collection.size()];
    int i = 0;
    for (var el :
        collection) {
      ((Object[]) data)[i] = el;
      i++;
    }
  }

  /**
   * Конструктор. Создаёт список, состоящий из переданного элемента.
   *
   * @param element элемент
   */
  public SmartList(V element) {
    data = element;
    size = 1;
  }

  /**
   * Конструктор. Создаёт список, состоящий из элементов переданного массива. Дальнейшие изменения в
   * переданном массиве не должны влиять на список.
   *
   * @param elements массив элементов
   */
  @SafeVarargs
  public SmartList(V... elements) {
    size = elements.length;
    data = elements.clone();
  }

  /**
   * Получение элемента по указанному индексу в списке
   *
   * @param index позиция в списке
   * @return элемент
   * @throws IndexOutOfBoundsException если индекс попадает за пределы списка
   *                                   ({@code index < 0 || index >= size()})
   */
  @Override
  public V get(int index) {
    Objects.checkIndex(index, size);
    if (size == 1) {
      return (V) data;
    }
    return ((V[]) data)[index];
  }

  /**
   * Вычисляет размер списка.
   *
   * @return количество элементов в списке
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Заменяет элемент по определенному индексу
   *
   * @param index позиция в списке
   * @return элемент, который находился по этому индексу ранее
   * @throws IndexOutOfBoundsException если индекс попадает за пределы списка
   *                                   ({@code index < 0 || index >= size()})
   */
  @Override
  public V set(int index, V element) {
    Objects.checkIndex(index, size);
    if (size == 1) {
      V oldValue = (V) data;
      data = element;
      return oldValue;
    }

    V oldValue = ((V[]) data)[index];
    ((V[]) data)[index] = element;
    return oldValue;
  }

  /**
   * Удаляет элемент по определенному индексу, сдвигая элементы, находящиеся правее удаляемого,
   * влево.
   *
   * @param index позиция в списке
   * @return удалённый элемент
   * @throws IndexOutOfBoundsException если индекс попадает за пределы списка
   *                                   ({@code index < 0 || index >= size()})
   */
  @Override
  public V remove(int index) {
    Objects.checkIndex(index, size);
    if (size == 1) {
      size = 0;
      V oldElement = (V) data;
      data = null;
      return oldElement;
    }
    V[] array = ((V[]) data);
    V oldElement = array[index];
    for (int i = index + 1; i < size; i++) {
      array[i - 1] = array[i];
    }
    --size;
    if (size == 1) {
      data = array[0];
    } else if (size * 4 < array.length) {
      V[] newArray = (V[]) new Object[size * 2];
      System.arraycopy(array, 0, newArray, 0, size);
      data = newArray;
    }
    return oldElement;
  }

  /**
   * Вставляет элемент в определенную позицию списка, сдвигая элементы вправо при необходимости.
   *
   * @param index   позиция в списке
   * @param element вставляемый элемент
   * @throws IndexOutOfBoundsException если индекс попадает за пределы списка
   *                                   ({@code index < 0 || index > size()})
   */
  @Override
  public void add(int index, V element) {
    Objects.checkIndex(index, size + 1);
    if (size == 0) {
      size = 1;
      data = element;
      return;
    }
    if (size == 1) {
      data = new Object[]{data};
    }
    Object[] array = (Object[]) data;
    if (size == array.length) {
      Object[] newArray = new Object[2 * size];
      System.arraycopy(data, 0, newArray, 0, index);
      newArray[index] = element;
      System.arraycopy(data, index, newArray, index + 1, size - index);
      data = newArray;
    } else {
      for (int i = size - 1; i >= index; i--) {
        array[i + 1] = array[i];
      }
      array[index] = element;
    }
    size++;
  }

  /**
   * Возвращает значение поля, в котором SmartList хранит свои данные. Выполнение этого метода не
   * должно влиять на состояние списка.
   * <p>
   * Этот метод должен выполняться за O(1) и не выполнять ни одной аллокации.
   * <p>
   * Этот метод нужен для того, чтобы мы могли протестировать вашу реализацию.
   *
   * @return значение поля, с помощью которого хранятся элементы списка.
   */
  protected Object getInternalData() {
    return data;
  }
}
