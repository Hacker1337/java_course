package org.csc.java.spring2023.commands;

import org.csc.java.spring2023.IOContext;
import org.csc.java.spring2023.Memory;

public class LoopCommand implements Command {

  private final Command afterLoopCommand;
  private Command intoLoopCommand;

  public LoopCommand(Command nextCommand) {
    this.afterLoopCommand = nextCommand;
  }

  public void setLoopBeginning(Command intoLoopCommand) {
    this.intoLoopCommand = intoLoopCommand;
  }

  @Override
  public void execute(IOContext context, Memory memory) {
    if (memory.getByte() > 0) {
      intoLoopCommand.execute(context, memory);
      return;
    }
    afterLoopCommand.execute(context, memory);
  }

}
