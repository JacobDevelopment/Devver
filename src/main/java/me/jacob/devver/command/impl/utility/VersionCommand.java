package me.jacob.devver.command.impl.utility;

import me.jacob.devver.command.Command;
import me.jacob.devver.command.CommandContext;
import net.dv8tion.jda.api.Permission;

public class VersionCommand extends Command {
	public VersionCommand() {
		super("version", "Displays versions of all associated bots.", new String[]{"v"}, Permission.MESSAGE_WRITE);
	}

	@Override
	public void run(CommandContext context, String[] args) {

	}
}
