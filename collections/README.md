# Задание №3. Collections.

Задание содержит в себе задачи на работу с Java Collections Framework

## Задачи

### 1. [HashBidiMap](src/main/java/org/csc/java/spring2023/bidimap/HashBidiMap.java)

Вам необходимо реализовать все методы, представленные в классе. В результате вы получите
ассоциативный массив (map) с двунаправленной ассоциацией между ключом и значением.

За полную реализацию вы получите **4 балла**.

### 2. [SmartList](src/main/java/org/csc/java/spring2023/smartlist/SmartList.java)

Вам необходимо реализовать все методы, представленные в классе. В результате вы получите
версию ArrayList, оптимизированную для хранения 0 и 1 элемента.

Обратите внимание на ограничения, описанные в Javadoc.

За полную реализацию вы получите **4 балла**.

### 3. [Iterable utils](src/main/java/org/csc/java/spring2023/iterables/IterableUtils.java)

Вам необходимо реализовать 4 функции, упрощающие:

* Обход диапазона целых чисел
* Обход диапазона целых чисел с указанным шагом
* Обход строки
* Обход нескольких коллекций одновременно

За полную реализацию вы получите **3 балла**. Стоимость каждой отдельной функции указана в Javadoc.

## Как сдавать

При создании вашего репозитория будет автоматически создан [Pull Request для проверки](../../pull/1).

Вы должны закоммитить своё решение в ветку `main` (это ветка вашего репозитория по-умолчанию), и эти
коммиты автоматически добавятся в Pull Request.

**Этот пулл-реквест не нужно мерджить и не нужно закрывать!!!**

Когда вы будете готовы сдать решение на проверку, добавьте к вашему пулл-реквесту лейбл `ready-for-review`. Это можно сделать в правой части страницы с пулл-реквестом.

**Важно:** перед отправкой на проверку убедитесь, что ваш код не содержит замечаний от `reviewdog`!
Просмотреть их вы можете на странице со своим пулл-реквестом.

В дальнейшем, если вы внесли исправления и хотите запросить очередную проверку, просто повторно
запросите ревью от преподавателя, который вас уже проверял:

![Анимация того, как запросить ревью](https://i.stack.imgur.com/H2XaO.gif)

Если у вас возникают проблемы на каком-то из этих шагов, пожалуйста, сообщите об этом в Slack. Чем
быстрее вы это сделаете, тем быстрее мы поможем вам.

## Дополнительные материалы

Здесь собраны данные, которые могут быть полезны при выполнении данного задания.

1. [Справочник по Java Collections Framework](https://habr.com/ru/post/237043/)
2. [Структуры данных в картинках. ArrayList](https://habr.com/ru/post/128269/)
2. [Структуры данных в картинках. HashMap](https://habr.com/ru/post/128017/)
3. [Структуры данных в Java. Полезные методы вспомогательных классов](https://habr.com/ru/company/epam_systems/blog/476098/)

### Итераторы

Итератор является объектом, который позволяет обойти некоторое множество элементов, абстрагируя
пользователя от особенностей природы этих элементов.

Пример итератора для обхода произвольного массива

```java
import java.util.Iterator;

class ArrayIterator<V> implements Iterator<V> {

  private final V[] array;
  private int current = 0;

  public ArrayIterator(V[] array) {
    this.array = array;
  }

  @Override
  public boolean hasNext() {
    return current < array.length;
  }

  @Override
  public V next() {
    return array[current++];
  }
}
```

Пример итератора, генерирующего случайные числа

```java
import java.util.Iterator;
import java.util.Random;

class RandomIterator implements Iterator<Integer> {

  private final int size;
  private final Random random = new Random();
  private int current = 0;

  public RandomIterator(int size) {
    this.size = size;
  }

  @Override
  public boolean hasNext() {
    return current < size;
  }

  @Override
  public Integer next() {
    current++;
    return random.nextInt();
  }
}
```
