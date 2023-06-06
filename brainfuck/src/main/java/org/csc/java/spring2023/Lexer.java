package org.csc.java.spring2023;

public interface Lexer {

  /**
   * Преобразует текст программы в упорядоченный набор токенов
   */
  Token[] tokenize(String programText);
}
