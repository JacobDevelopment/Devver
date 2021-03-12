package me.jacob.devver.command;

import me.jacob.devver.Config;
import me.jacob.devver.command.impl.utility.TestCommand;
import me.jacob.devver.utility.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandRegistry {

	private final Map<String, Command> COMMAND_MAP = new HashMap<>();
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	public CommandRegistry() {
		putCommands(new TestCommand());
	}

	public void run(GuildMessageReceivedEvent event) {
		executorService.submit(() -> {
			if (event.getAuthor().isBot() || event.isWebhookMessage())
				return;

			if (event.getMember() == null)
				return;


			final String contentRaw = event.getMessage().getContentRaw().substring(Constants.PREFIX.length());
			final String[] args = contentRaw.split("\\s+");

			final Command command = getCommand(args[0].toLowerCase());
			if (command == null)
				return;

			final String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
			command.run(new CommandContext(event), finalArgs);
		});
	}

	public Command getCommand(String name) {
		return COMMAND_MAP.get(name);
	}

	private void putCommands(Command... commands) {
		for (Command command : commands)
			putCommand(command);
	}

	private void putCommand(Command command) {
		COMMAND_MAP.put(command.getName(), command);
		for (String alias : command.getAliases())
			COMMAND_MAP.put(alias, command);
	}

}
