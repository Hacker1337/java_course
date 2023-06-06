# Задание №2. Brainfuck

[Brainfuck](https://en.wikipedia.org/wiki/Brainfuck) -- тьюринг-полный язык программирования,
придуманный Урбаном Мюллером. Язык содержит всего восемь команд `><+-.,[]`. Программа на языке
Brainfuck работает с простой машиной
(похожей на машину Тьюринга), состоящей из набора ячеек памяти и указателя на текущую ячейку.

Описание команд:

* `>` Передвинуть указатель в следующую ячейку массива.
* `<` Передвинуть указатель в предыдущую ячейку массива.
* `+` Увеличить значение в текущей ячейке массива на единицу.
* `-` Уменьшить значение в текущей ячейке массива на единицу.
* `.` Вывести текущее значение ячейки массива в выходной поток.
* `,` Прочитать значение из входного потока.
* `[` Перейти к инструкции, следующей за `]`, если значение в текущей ячейке равно нулю, иначе
  перейти к следующей по порядку инструкции.
* `]` Перейти на парную `[`.

В описанных ниже задачах нужно будет реализовать ~~over-engineered~~ production-ready версию
интерпретатора Brainfuck. Интерпретатор будет состоять из шести частей:

1) Лексер -- превращает текст программы в набор токенов
2) Линтер -- проверяет набор токенов на корректность и выводит сообщения об ошибках
3) Парсер -- принимает на вход корректный набор токенов и превращает их в набор команд
4) Устройство памяти -- сущность, которая полностью инкапсулирует логику работы с памятью машины
   Brainfuck
5) Устройство ввода-вывода -- сущность, позволяющая читать из входного потока и писать в выходной
   поток
   (т.к. на лекциях ввод-вывод будет еще не скоро, мы предоставим готовый код)
6) Интерпретатор -- использует все написанные ранее запчасти и исполняет программу

Важно отметить, что описанный подход не является оптимальным или единственным. Это задание не на
написание самого оптимального и простого интерпретатора, а на создание удобной, расширяемой системы.
Потенциально возможные расширения -- дебаггер, трассирующий интерпретатор, etc.

Мы предоставим вам набор интерфейсов, отвечающих за определенные части интерпретатора. Ваша задача
-- написать классы, которые удовлетворяют этим интерфейсам.

### 1. Реализуйте `Lexer` [1 балл]

Единственный метод `tokenize` принимает на вход текст программы и превращает его в массив `Token`
-ов. Перечисление `Token`, помимо полного дублирования команд языка содержит значение
`UNRECOGNIZED_TOKEN`, которое используется, чтобы помечать некорректные символы и в дальнейшем
обрабатывать их в
`Linter`-е.

Экземпляр `Lexer` нужно не забыть вернуть в методе `Interpreter#createLexer`.

### 2. Реализуйте `Linter` [2 балла]

В качестве проверяемых правил нужно реализовать как минимум эти два:

1. Отсутствие неизвестных символов в коде программы
2. Правильность скобочной последовательности для циклов: `[[]][]` -- правильная
   последовательность; `[]]` -- неправильная

Для каждого вида проблем нужно написать свой класс-наследник `LintProblem` и придумать
диагностическую информацию, которую он будет возвращать в методе `getMessage`.

Метод `Linter#lint` должен принимать на вход массив `Token`-ов и возвращать список проблем с
подробным описанием.

Экземпляр `Linter` нужно не забыть вернуть в методе `Interpreter#createLinter`.

### 3. Реализуйте `Memory` [1 балл]

`Memory` -- простая обертка над массивом байтов. Когда пользователь пытается выйти за границы
массива, должно бросаться runtime-исключение `MemoryAccessException` (оно будет вам предоставлено).

Экземпляр `Memory` нужно не забыть вернуть в методе `Interpreter#createMemory`.

### 4. Команды и парсинг [6 баллов]

Команда -- сущность, которая умеет исполнять себя с помощью метода `execute`.

Команда может отвечать за выполнение одной элементарной инструкции (например, команды `>`), за
сложную языковую конструкцию (например, выполнение цикла корректное количество раз), или за всю
программу целиком.

`Parser#parse` преобразует набор токенов в исполняемую команду.

Есть много способов реализации `Parser#parse`, мы опишем два из них. Можно реализовать один из
предложенных способов, либо придумать свой.

**Первый способ** -- создать главную команду `Program`, которая хранит в себе список команд. Все
команды, кроме команд цикла имеют тривиальную реализацию без состояния. Для цикла будет
использоваться одна команда `LoopCommand`, которая хранит в себе список команд тела цикла. Тогда,
для корректной работы надо сделать так, чтобы тело цикла не попало в команду `Program`. Аналогично
для вложенных циклов -- тело вложенного цикла не должно попасть в список команд тела внешнего цикла.

**Второй способ** -- не заводить главной команды, а хранить следующую команду в поле текущей. Таким
образом, метод `Command#execute` будет сначала выполнять свою логику, а потом передавать управление
следующей команде. Тогда, команда цикла `LoopCommand` будет иметь два поля -- ссылку на первую
команду тела цикла и ссылку на первую команду после `RIGHT_BRACKET`. Возможно, пригодится завести
пустую команду `EmptyCommand`.

### 5. Вуншпунш

Все запчасти готовы, осталось собрать их вместе. Реализуйте статический метод `run` в
классе `Interpreter` и не забудьте бросить `IllegalArgumentException` с подробным описанием ошибок,
если текст программы не соответствует правилам языка.

Если у вас возникли проблемы со "связыванием" отдельных кусков интерпретатора вместе, вернитесь к
описанию задачи в начале `README`.

## Как сдавать

При создании вашего репозитория будет автоматически создан [Pull Request для проверки](../../pull/1).

Вы должны закоммитить своё решение в ветку `main` (это ветка вашего репозитория по-умолчанию), и эти
коммиты автоматически добавятся в Pull Request.

**Этот пулл-реквест не нужно мерджить и не нужно закрывать!!!**
            
Когда вы будете готовы сдать решение на проверку, добавьте к вашему пулл-реквесту лейбл `ready-for-review``. Это можно сделать в правой части страницы с пулл-реквестом.
            
В дальнейшем, если вы внесли исправления и хотите запросить очередную проверку, просто повторно запросите ревью от преподавателя, который вас уже проверял:
            
![Анимация того, как запросить ревью](https://i.stack.imgur.com/H2XaO.gif)

Если у вас возникают проблемы на каком-то из этих шагов, пожалуйста, сообщите об этом в Slack. Чем
быстрее вы это сделаете, тем быстрее мы поможем вам.

## Дополнительные материалы

Здесь собраны данные, которые могут быть полезны при выполнении данного задания.

### Списки

В `Java` интерфейс `List` описывает упорядоченную последовательность элементов. Главные методы для
работы со списком –
`get(index)`, `add(element)`, `size()`. Важные реализации списка – список на основе массива и
связный список,
`ArrayList` и `LinkedList` соответственно. В этом задании предполагается использование `ArrayList`

**Пример:**

```java
import java.util.ArrayList;
import java.util.List;

class ArrayListExample {

  static void example() {
    List<Command> commands = new ArrayList<>();
    commands.add(new NextCommand());
    System.out.println("number of commands = " + commands.size());
    if (!commands.isEmpty()) {
      System.out.println("first command is = " + commands.get(0).getName());
    }
  }

  interface Command {

    String getName();
  }

  static class NextCommand implements Command {

    public String getName() {
      return "NextCommand";
    }
  }
}
```