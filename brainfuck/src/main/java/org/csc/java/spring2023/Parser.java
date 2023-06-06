package org.csc.java.spring2023;

import org.csc.java.spring2023.commands.Command;

public interface Parser {

  /**
   * Преобразует токены в исполняемую программу. Можно полагаться на то, что токены уже проверены на
   * корректность с помощью Linter
   */
  Command parse(Token[] tokens);
}
