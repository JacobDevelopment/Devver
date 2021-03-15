package me.jacob.devver.command;

import me.jacob.devver.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CommandContext {

	private final GuildMessageReceivedEvent event;
	private final Config config;
	private final String invoker;

	public CommandContext(GuildMessageReceivedEvent event, Config config, String invoker) {
		this.event = event;
		this.config = config;
		this.invoker = invoker;
	}

	public GuildMessageReceivedEvent getEvent() {
		return event;
	}

	public Config getConfig() {
		return config;
	}

	public String getInvoker() {
		return invoker;
	}

	public Guild getGuild() {
		return this.getEvent().getGuild();
	}

	public TextChannel getChannel() {
		return this.getEvent().getChannel();
	}

	public Message getMessage() {
		return this.getEvent().getMessage();
	}

	public Member getMember() {
		return this.getEvent().getMember();
	}

	public User getAuthor() {
		return this.getEvent().getAuthor();
	}

	public List<TextChannel> getMentionedChannels() {
		return this.getMessage().getMentionedChannels();
	}

	public TextChannel getMentionedChannel() {
		return getMentionedChannels().size() == 1 ? getMentionedChannels().get(0) : null;
	}

	public void reply(String content, int secondsDelay) {
		getChannel().sendMessage(content)
				.delay(secondsDelay, TimeUnit.SECONDS)
				.flatMap(Message::delete)
				.queue();
	}

	public void reply(String content) {
		getChannel().sendMessage(content).queue();
	}

	public void reply(EmbedBuilder embedBuilder, int secondsDelay) {
		getChannel().sendMessage(embedBuilder.build())
				.delay(secondsDelay, TimeUnit.SECONDS)
				.flatMap(Message::delete)
				.queue();
	}

	public void reply(EmbedBuilder embedBuilder) {
		getChannel().sendMessage(embedBuilder.build()).queue();
	}

	public void reply(Consumer<EmbedBuilder> embedBuilderConsumer) {
		final EmbedBuilder embedBuilder = new EmbedBuilder().setColor(0x34495e);
		embedBuilderConsumer.accept(embedBuilder);
		reply(embedBuilder);
	}

	public void reply(Consumer<EmbedBuilder> embedBuilderConsumer, int secondsDelay) {
		final EmbedBuilder embedBuilder = new EmbedBuilder().setColor(0x34495e);
		embedBuilderConsumer.accept(embedBuilder);
		reply(embedBuilder, secondsDelay);
	}


}

