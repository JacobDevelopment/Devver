package me.jacob.devver.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class CommandContext {

	private final GuildMessageReceivedEvent event;

	public CommandContext(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	public GuildMessageReceivedEvent getEvent() {
		return event;
	}

	public TextChannel getChannel() {
		return this.getEvent().getChannel();
	}

	public Member getMember() {
		return this.getEvent().getMember();
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

}

