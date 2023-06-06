package org.csc.java.spring2023;

import java.text.MessageFormat;
import java.util.List;
import org.csc.java.spring2023.commands.Command;
import org.csc.java.spring2023.lint.Linter;
import org.csc.java.spring2023.lint.LinterImpl;

public class Interpreter {

  private static final int memorySize = 1 << 10;

  /**
   * Исполняет программу programText, написанную на языке Brainfuck, используя ioContext в качестве
   * устройства ввода-вывода. Для отладки предлагается передавать ConsoleIOContext, он использует
   * System.in и System.out
   *
   * @throws IllegalArgumentException если на вход дан некорректный текст программы. Текст сообщения
   *                                  – конкатенация сообщений об ошибках LintProblem#getMessage
   */
  public static void run(IOContext ioContext, String programText) throws IllegalArgumentException {
    Lexer lexer = createLexer();
    Linter linter = createLinter();

    Token[] tokenized = lexer.tokenize(programText);

    checkProblems(programText, tokenized, linter);

    Parser parser = createParser();
    Memory memory = createMemory(memorySize);

    Command program = parser.parse(tokenized);
    program.execute(ioContext, memory);


  }

  private static void checkProblems(String programText, Token[] tokenized, Linter linter) {
    List<LintProblem> problems = linter.lint(tokenized, programText);
    if (!problems.isEmpty()) {
      StringBuilder allMessages = new StringBuilder(MessageFormat.format("""
          The text of the program does not comply with the rules of the language
          Found {0} problems:
          """, problems.size()));
      for (var problem : problems) {
        allMessages.append(problem.getMessage()).append(System.lineSeparator());
      }
      throw new IllegalArgumentException(allMessages.toString());
    }
  }

  public static Lexer createLexer() {
    return new LexerImpl();
  }

  public static Parser createParser() {
    return new ParserImpl();
  }

  public static Memory createMemory(int memorySize) {
    return new MemoryImpl(memorySize);
  }

  public static Linter createLinter() {
    return new LinterImpl();
  }
}
