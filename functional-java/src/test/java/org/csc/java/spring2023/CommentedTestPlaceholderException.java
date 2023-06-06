package org.csc.java.spring2023;

public class CommentedTestPlaceholderException extends AssertionError {

  private static final String MESSAGE_ABOUT_PLACEHOLDER = """
      Это исключение используется как заглушка, чтобы тесты не мешали вам компилировать код.
      Под тем местом, где было выброшено это исключение, есть закомментированный тест.
      Когда вы решите пройти тест, удалите выбрасывание исключения и раскомментируйте код под ним.
      """;

  public CommentedTestPlaceholderException() {
    super(MESSAGE_ABOUT_PLACEHOLDER);
  }

}
