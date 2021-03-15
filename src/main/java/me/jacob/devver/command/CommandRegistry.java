package me.jacob.devver.command;

import me.jacob.devver.Config;
import me.jacob.devver.command.impl.admin.AnnounceCommand;
import me.jacob.devver.command.impl.utility.*;
import me.jacob.devver.utility.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CommandRegistry {

	private final Map<String, Command> COMMAND_MAP = new HashMap<>();
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	public CommandRegistry() {
		synchronized (this) {
			putCommands(
					new HelpCommand(this),
					new AnnounceCommand(),
					new SuggestionCommand(),
					new VersionCommand(),
					new TestCommand(),
					new GitHubCommand()
			);
		}
	}

	public void run(GuildMessageReceivedEvent event, Config config) {
		executorService.execute(() -> {
			if (event.getAuthor().isBot() || event.isWebhookMessage())
				return;

			if (event.getMember() == null)
				return;

			final String contentRaw = event.getMessage().getContentRaw().substring(Constants.PREFIX.length());
			final String[] args = contentRaw.split("\\s+");

			final Command command = getCommand(args[0].toLowerCase());
			if (command == null)
				return;

			event.getMessage().delete().queue(null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_MESSAGE));

			final String[] finalArgs = Arrays.copyOfRange(args, 1, args.length);
			command.run(new CommandContext(event, config, args[0].toLowerCase()), finalArgs);
		});
	}

	public Command getCommand(String name) {
		return COMMAND_MAP.get(name);
	}

	public void putCommands(Command... commands) {
		for (Command command : commands)
			putCommand(command);
	}

	private void putCommand(Command command) {
		COMMAND_MAP.put(command.getName(), command);
		if (command.getAliases() == null)
			return;

		for (String alias : command.getAliases())
			COMMAND_MAP.put(alias, command);
	}

	public List<Command> getCommands(Member member) {
		return COMMAND_MAP.values()
				.stream()
				.distinct()
				.filter(command -> member.hasPermission(command.getPermissions()))
				.sorted(Comparator.comparing(Command::getName))
				.collect(Collectors.toUnmodifiableList());
	}

}
