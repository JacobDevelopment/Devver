package me.jacob.devver.command.impl.utility;

import me.jacob.devver.command.Command;
import me.jacob.devver.command.CommandContext;

import java.util.List;

public class TestCommand extends Command {


	public TestCommand() {
		super("test", "A testing command, obviously.");
	}

	@Override
	public void run(CommandContext context, String[] args) {
		context.reply("I am working... I think?", 10);
	}
}
