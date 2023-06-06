package org.csc.java.spring2023.commands;

import org.csc.java.spring2023.IOContext;
import org.csc.java.spring2023.Memory;

public class NextCommand implements Command {

  private final Command nextCommand;

  public NextCommand(Command nextCommand) {
    this.nextCommand = nextCommand;
  }

  @Override
  public void execute(IOContext context, Memory memory) {
    memory.shiftRight();
    nextCommand.execute(context, memory);
  }

}
