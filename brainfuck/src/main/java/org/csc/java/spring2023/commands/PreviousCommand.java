package org.csc.java.spring2023.commands;

import org.csc.java.spring2023.IOContext;
import org.csc.java.spring2023.Memory;

public class PreviousCommand implements Command {

  private final Command nextCommand;

  public PreviousCommand(Command nextCommand) {
    this.nextCommand = nextCommand;
  }

  @Override
  public void execute(IOContext context, Memory memory) {
    memory.shiftLeft();
    nextCommand.execute(context, memory);
  }

}
