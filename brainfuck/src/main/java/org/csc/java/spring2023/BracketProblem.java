package org.csc.java.spring2023;

import java.text.MessageFormat;

public class BracketProblem implements LintProblem {

  private final int position;
  private final String codeQuote;

  public BracketProblem(int position, String codeQuote) {
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
        Unmatched bracket found at position {1}""";
    return MessageFormat.format(message, codeQuote, position);
  }
}
