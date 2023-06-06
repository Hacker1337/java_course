package org.csc.java.spring2023.lint;

import java.util.List;
import org.csc.java.spring2023.LintProblem;
import org.csc.java.spring2023.Token;

public interface Linter {

  /**
   * Проверяет программу на наличие ошибок и возвращает список с детальной информацией о проблемах
   */
  List<LintProblem> lint(Token[] tokens, String programText);
}
