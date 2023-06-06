package org.csc.java.spring2023.commands;

import org.csc.java.spring2023.IOContext;
import org.csc.java.spring2023.Memory;

public class InputCommand implements Command {

  private final Command nextCommand;

  public InputCommand(Command nextCommand) {
    this.nextCommand = nextCommand;
  }

  @Override
  public void execute(IOContext context, Memory memory) {
    memory.setByte(context.readByte());
    nextCommand.execute(context, memory);
  }

}
