package org.csc.java.spring2023.lint;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;
import org.csc.java.spring2023.BracketProblem;
import org.csc.java.spring2023.LintProblem;
import org.csc.java.spring2023.Token;
import org.csc.java.spring2023.UnrecognizedTokenProblem;

public class LinterImpl implements Linter {

  /**
   * Cuts part of program code with a problem to show it to user.
   *
   * @param i position of bed Token
   * @return part of code with error symbol on 4-th position
   */
  private static String getCodeQuote(int i, String programText) {
    int endIndex = min(programText.length(), i + 6);
    if (i >= 4) {
      return programText.substring(i - 4, endIndex);
    } else {
      return " ".repeat(4 - i) + programText.substring(0, endIndex);
    }
  }

  private static void checkClosingBrackets(Token[] tokens, String programText,
      List<LintProblem> problems) {
    List<Integer> openBrackets = new ArrayList<>(); // позиции не закрытых открывающихся скобок
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i] == Token.LEFT_BRACKET) {
        openBrackets.add(i);
      }
      if (tokens[i] == Token.RIGHT_BRACKET) {
        if (!openBrackets.isEmpty()) {
          openBrackets.remove(openBrackets.size() - 1);
        } else {
          problems.add(new BracketProblem(i, getCodeQuote(i, programText)));
        }
      }
    }
    for (int i : openBrackets) {
      problems.add(new BracketProblem(i, getCodeQuote(i, programText)));
    }
  }

  private static void checkUnsupportedTokens(Token[] tokens, String programText,
      List<LintProblem> problems) {
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i] == Token.UNRECOGNIZED_TOKEN) {
        problems.add(new UnrecognizedTokenProblem(i, getCodeQuote(i, programText)));
      }
    }
  }

  @Override
  public List<LintProblem> lint(Token[] tokens, String programText) {
    List<LintProblem> problems = new ArrayList<>();

    checkClosingBrackets(tokens, programText, problems);
    checkUnsupportedTokens(tokens, programText, problems);

    return problems;
  }
}
