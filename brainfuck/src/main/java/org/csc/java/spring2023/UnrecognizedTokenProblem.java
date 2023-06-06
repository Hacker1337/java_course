package org.csc.java.spring2023;

import java.text.MessageFormat;

public class UnrecognizedTokenProblem implements LintProblem {

  private final int position;
  private final String codeQuote;

  public UnrecognizedTokenProblem(int position, String codeQuote) {
    this.position = position;
    this.codeQuote = codeQuote;
  }

  @Override
  public int getPosition() {
    return position;
  }

  @Override
  public String getMessage() {
    String message = """
        {0}
            ^
        Unrecognized symbol found at position {1}""";
    return MessageFormat.format(message, codeQuote, position);
  }
}
