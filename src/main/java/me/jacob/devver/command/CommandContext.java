package me.jacob.devver.command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandContext {

	private final GuildMessageReceivedEvent event;

	public CommandContext(GuildMessageReceivedEvent event) {
		this.event = event;
	}

	public GuildMessageReceivedEvent getEvent() {
		return event;
	}


}

