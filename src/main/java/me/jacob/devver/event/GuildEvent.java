package me.jacob.devver.event;

import me.jacob.devver.Config;
import me.jacob.devver.command.CommandRegistry;
import me.jacob.devver.command.impl.admin.AnnounceCommand;
import me.jacob.devver.command.impl.utility.*;
import me.jacob.devver.utility.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildEvent extends ListenerAdapter {

	private final Config config;

	private final CommandRegistry commandRegistry;

	public GuildEvent(Config config) {
		this.config = config;
		this.commandRegistry = new CommandRegistry();
	}

	/**
	 * We will utilize the {@link GuildMessageReceivedEvent} to orchestrate
	 * command handling until SlashCommands are properly implemented
	 * within JDA and there are no prohibiting bugs.
	 *
	 * @param event - Messages we receive in the channels.
	 */
	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot() || event.isWebhookMessage())
			return;

		if (isBlacklisted(event.getAuthor().getIdLong()))
			return;

		final String contentRaw = event.getMessage().getContentRaw();
		if (contentRaw.startsWith(Constants.PREFIX)) {
			commandRegistry.run(event, config);
		}
	}

	/**
	 * At the event Devver is somehow added to a server, and isn't
	 * caught in the {@link GuildJoinEvent}, we will leave based on the
	 * {@link GuildReadyEvent}.
	 *
	 * @param event - When a guild is marked ready via discord.
	 */
	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		final long guildId = event.getGuild().getIdLong();
		if (guildId != config.getBuiltInstance().getLong("server_id"))
			event.getGuild().leave().queue();
	}

	/**
	 * Ultimately this event will never fire, but other precautions were added
	 * in case an unlikely event occurred: which would be the bot joining a guild
	 * other than the ones owned by me. It is also disabled in the developer
	 * portal that no one can invite this bot unless it's by my user itself.
	 *
	 * @param event - The event in which the self member (Devver) joins a guild.
	 */
	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event) {
		final TextChannel logChannel = event.getJDA().getTextChannelById(config.getBuiltInstance().getLong("log_channel"));
		if (logChannel == null)
			return;

		final long guildId = event.getGuild().getIdLong();
		final long mainId = config.getBuiltInstance().getLong("server_id");

		if (guildId != mainId) {
			event.getGuild().retrieveOwner()
					.map(Member::getUser)
					.queue(this::sendPrivateMessage, throwable -> {
						logChannel.sendMessageFormat("I tried dm'ing the owner, but It failed! (Guild: %s)", guildId).queue();
					});

			event.getGuild().leave().queue(
					success -> logChannel.sendMessageFormat("Successfully left %s!", guildId).queue(),
					error -> logChannel.sendMessageFormat("Couldn't leave %s due to an error!", guildId).queue()
			);
		}

	}

	/**
	 * @param user - The user we are private messaging in this case.
	 */
	private void sendPrivateMessage(User user) {
		user.openPrivateChannel()
				.flatMap(privateChannel -> privateChannel.sendMessage("You are not allowed to use this bot!"))
				.queue();
	}


	/**
	 * This method checks if the member sending a message
	 * is blacklisted via the config.json file, and if so
	 * the message will ultimately be ignored and won't
	 * be registered as a command if possible.
	 *
	 * @param memberId - The id of a {@link Member} that sent a message.
	 * @return - True or false.
	 */
	private boolean isBlacklisted(long memberId) {
		for (Object object : config.getBuiltInstance().getArray("blacklisted_ids"))
			if ((long) object == memberId)
				return true;
		return false;
	}
}
