package org.csc.java.spring2023;

/**
 * Дескриптор проблемы
 */
public interface LintProblem {

  /**
   * @return Позиция проблемы в исходном тексте программы. Отсчет идет с нуля.
   */
  int getPosition();

  /**
   * @return Подробное описание проблемы, которое можно было бы показать пользователю.
   */
  String getMessage();
}
