package me.jacob.devver.command.impl.admin;

import me.jacob.devver.Config;
import me.jacob.devver.command.Command;
import me.jacob.devver.command.CommandContext;
import me.jacob.devver.utility.Constants;
import me.jacob.devver.utility.StringUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class AnnounceCommand extends Command {

	public AnnounceCommand() {
		super(
				"announce",
				"Sends a message to the announcement channel.",
				new String[]{"ann"},
				Permission.ADMINISTRATOR
		);
	}

	@Override
	public void run(CommandContext context, String[] args) {
		if (args.length == 0) {
			context.reply(embedBuilder -> embedBuilder
					.setColor(Color.RED)
					.setDescription("You did not provide an announcement message!")
			);
			return;
		}

		final Config config = context.getConfig();
		final long announcementId = config.getBuiltInstance().getLong("announcement_id", 0);

		if (announcementId == 0) {
			context.reply(embedBuilder -> embedBuilder
					.setColor(Color.RED)
					.setDescription("The announcement channel was not configured properly.")
			);
			return;
		}

		final TextChannel announceChannel = context.getGuild().getTextChannelById(announcementId);
		if (announceChannel == null) {
			context.reply(embedBuilder -> embedBuilder
					.setColor(Color.RED)
					.setDescription("The provided ID does not return a valid text channel.")
			);
			return;
		}

		final String announcement = context.getMessage()
				.getContentRaw()
				.substring(Constants.PREFIX.length() + context.getInvoker().length());

		sendAnnouncement(announceChannel, announcement, config);
	}

	private void sendAnnouncement(TextChannel textChannel, String announcement, Config config) {
		final Member self = textChannel.getGuild().getSelfMember();
		if (!textChannel.canTalk(self))
			return;

		final long roleId = config.getBuiltInstance().getLong("announcement_role_id", 0);
		final Role announcementRole = textChannel.getGuild().getRoleById(roleId);

		if (announcementRole != null) {
			textChannel.sendMessage(announcementRole.getAsMention()).queue();
		}
		textChannel.sendMessage(announcement).queue();
	}
}
