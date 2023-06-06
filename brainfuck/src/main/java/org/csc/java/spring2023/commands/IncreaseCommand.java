package org.csc.java.spring2023.commands;

import org.csc.java.spring2023.IOContext;
import org.csc.java.spring2023.Memory;

public class IncreaseCommand implements Command {

  private final Command nextCommand;

  public IncreaseCommand(Command nextCommand) {
    this.nextCommand = nextCommand;
  }

  @Override
  public void execute(IOContext context, Memory memory) {
    memory.setByte((byte) (memory.getByte() + 1));
    nextCommand.execute(context, memory);
  }

}
