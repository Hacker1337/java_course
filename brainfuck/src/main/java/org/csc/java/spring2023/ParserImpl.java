package org.csc.java.spring2023;

import java.util.ArrayList;
import java.util.List;
import org.csc.java.spring2023.commands.Command;
import org.csc.java.spring2023.commands.DecreaseCommand;
import org.csc.java.spring2023.commands.EmptyCommand;
import org.csc.java.spring2023.commands.IncreaseCommand;
import org.csc.java.spring2023.commands.InputCommand;
import org.csc.java.spring2023.commands.LoopCommand;
import org.csc.java.spring2023.commands.NextCommand;
import org.csc.java.spring2023.commands.OutputCommand;
import org.csc.java.spring2023.commands.PreviousCommand;

public class ParserImpl implements Parser {

  /**
   * Преобразует токены в исполняемую программу. Можно полагаться на то, что токены уже проверены на
   * корректность с помощью Linter
   */
  @Override
  public Command parse(Token[] tokens) {
    // читаем код программы задом наперед

    Command currentCommand = new EmptyCommand();
    List<LoopCommand> bracketsStack = new ArrayList<>();
    for (int i = tokens.length - 1; i >= 0; i--) {
      // создаем новую команду, ссылающуюся на следующую
      currentCommand = switch (tokens[i]) {
        case NEXT -> new NextCommand(currentCommand);
        case PREVIOUS -> new PreviousCommand(currentCommand);
        case INCREASE -> new IncreaseCommand(currentCommand);
        case DECREASE -> new DecreaseCommand(currentCommand);
        case OUTPUT -> new OutputCommand(currentCommand);
        case INPUT -> new InputCommand(currentCommand);
        case RIGHT_BRACKET -> {
          LoopCommand loopCommand = new LoopCommand(currentCommand);
          bracketsStack.add(loopCommand);
          yield loopCommand;
        }
        case LEFT_BRACKET -> {
          int lastBracketIndex = bracketsStack.size() - 1;
          LoopCommand lastBracket = bracketsStack.get(lastBracketIndex);
          lastBracket.setLoopBeginning(currentCommand);
          bracketsStack.remove(lastBracketIndex);
          yield lastBracket;
        }
        default -> throw new IllegalArgumentException("Didn't expect unknown symbol after linting");
      };
    }
    return currentCommand;  // первая команда в программе
  }
}
