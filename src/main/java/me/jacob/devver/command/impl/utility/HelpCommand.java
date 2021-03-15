package me.jacob.devver.command.impl.utility;

import me.jacob.devver.command.Command;
import me.jacob.devver.command.CommandContext;
import me.jacob.devver.command.CommandRegistry;
import me.jacob.devver.utility.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.List;

public class HelpCommand extends Command {

	private final CommandRegistry commandRegistry;

	public HelpCommand(CommandRegistry commandRegistry) {
		super("help", "Displays a list of commands.", new String[]{"h"}, Permission.MESSAGE_WRITE);
		this.commandRegistry = commandRegistry;
	}

	@Override
	public void run(CommandContext context, String[] args) {
		final List<Command> commandList = commandRegistry.getCommands(context.getMember());
		if (commandList.isEmpty()) {
			context.reply(embedBuilder -> embedBuilder
					                              .setColor(Color.RED)
					                              .setDescription("It seems you do not have permission to view any commands!"), 15);
			return;
		}

		if (args.length == 0) {
			context.reply(buildHelper(commandList), 30);
			return;
		}

		final String commandName = args[0].toLowerCase();
		final Command command = commandRegistry.getCommand(commandName);

		if (command == null) {
			context.reply(embedBuilder -> embedBuilder
					                              .setColor(Color.RED)
					                              .setDescription("No command with that name exists!"), 10
			);
			return;
		}

		context.reply(new CommandHelper(command).build(), 30);
	}

	private EmbedBuilder buildHelper(List<Command> commandList) {
		final StringBuilder stringBuilder = new StringBuilder();
		final int longestName = getLongestName(commandList);


		for (Command command : commandList) {
			stringBuilder
					.append("`").append(Constants.PREFIX).append("help ")
					.append(StringUtils.rightPad(command.getName(), longestName, " "))
					.append("`").append("-").append("`").append(command.getDescription())
					.append("`\n");
		}

		return new EmbedBuilder()
				       .setDescription(String.format("Type **%shelp <command_name>** to view a command!\n%s",
						       Constants.PREFIX, stringBuilder));
	}

	private int getLongestName(List<Command> commandList) {
		int size = 0;
		for (Command command : commandList) {
			if (command.getName().length() > size)
				size = command.getName().length();
		}
		return size + 1;
	}

	private static class CommandHelper {
		private final Command command;

		private CommandHelper(Command command) {
			this.command = command;
		}

		public EmbedBuilder build() {
			return new EmbedBuilder()
					       .setDescription(String.format(
							       """
							       **Command Name:** %s
							       **Description:** 
							       ```%s```
							       """
							       , command.getName(), command.getDescription())
					       )
					       .addField("Aliases:", "`" + getAliases() + "`", true)
					       .addField("Permissions:", "`" + getPermissions() + "`", true);
		}

		public String getAliases() {
			if (command.getAliases().length == 0 || command.getAliases() == null)
				return "No aliases exist.";
			return String.join(", ", command.getAliases());
		}

		public String getPermissions() {
			return String.join(", ", command.getPermission().toString());
		}

	}

}
