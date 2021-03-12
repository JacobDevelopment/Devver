package me.jacob.devver.command.impl.utility;

import me.jacob.devver.command.Command;
import me.jacob.devver.command.CommandContext;
import me.jacob.devver.utility.StringUtil;
import net.dv8tion.jda.api.Permission;


public class GitHubCommand extends Command {

	public GitHubCommand() {
		super("github", "Shows all github links.", new String[]{"gh"}, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_WRITE);
	}

	@Override
	public void run(CommandContext context, String[] args) {
		context.reply(embedBuilder -> embedBuilder
				.addField("DiscordBot", StringUtil.linkMarkdown("Click Me", "https://www.github.com"), true)
				.addField("Devver", StringUtil.linkMarkdown("Click Me", "https://www.github.com"), true),
		10);
	}
}
