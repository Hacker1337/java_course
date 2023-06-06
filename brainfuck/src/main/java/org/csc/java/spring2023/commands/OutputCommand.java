package org.csc.java.spring2023.commands;

import org.csc.java.spring2023.IOContext;
import org.csc.java.spring2023.Memory;

public class OutputCommand implements Command {

  private final Command nextCommand;

  public OutputCommand(Command nextCommand) {
    this.nextCommand = nextCommand;
  }

  @Override
  public void execute(IOContext context, Memory memory) {
    context.writeByte(memory.getByte());
    nextCommand.execute(context, memory);
  }

}
