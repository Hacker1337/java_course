package org.csc.java.spring2023;

import static org.csc.java.spring2023.Token.DECREASE;
import static org.csc.java.spring2023.Token.INCREASE;
import static org.csc.java.spring2023.Token.INPUT;
import static org.csc.java.spring2023.Token.LEFT_BRACKET;
import static org.csc.java.spring2023.Token.NEXT;
import static org.csc.java.spring2023.Token.OUTPUT;
import static org.csc.java.spring2023.Token.PREVIOUS;
import static org.csc.java.spring2023.Token.RIGHT_BRACKET;
import static org.csc.java.spring2023.Token.UNRECOGNIZED_TOKEN;

public class LexerImpl implements Lexer {

  @Override
  public Token[] tokenize(String programText) {
    Token[] tokenizedProgram = new Token[programText.length()];
    for (int i = 0; i < programText.length(); i++) {
      tokenizedProgram[i] = switch (programText.charAt(i)) {
        case '>' -> NEXT;
        case '<' -> PREVIOUS;
        case '+' -> INCREASE;
        case '-' -> DECREASE;
        case '.' -> OUTPUT;
        case ',' -> INPUT;
        case '[' -> LEFT_BRACKET;
        case ']' -> RIGHT_BRACKET;
        default -> UNRECOGNIZED_TOKEN;
      };
    }
    return tokenizedProgram;
  }
}
