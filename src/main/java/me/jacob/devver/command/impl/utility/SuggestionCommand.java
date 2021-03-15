package me.jacob.devver.command.impl.utility;

import me.jacob.devver.command.Command;
import me.jacob.devver.command.CommandContext;
import me.jacob.devver.utility.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;

public class SuggestionCommand extends Command {

    public SuggestionCommand() {
        super("suggestion", "Submits a suggestion in regards to DiscordBot.", new String[]{"suggest"}, Permission.MESSAGE_WRITE);
    }

    @Override
    public void run(CommandContext context, String[] args) {
        final long suggestionId = context.getConfig().getBuiltInstance().getLong("suggestion_id", 0);
        if (suggestionId == 0) {
            context.reply(embedBuilder -> embedBuilder
                            .setColor(Color.RED)
                            .setDescription("The suggestion channel is not configured."),
                    15);
            return;
        }

        if (context.getChannel().getIdLong() != suggestionId) {
            context.reply(embedBuilder -> embedBuilder
                            .setColor(Color.RED)
                            .setDescription("This is not the suggestion channel, please do not use this command here!"),
                    15);
            return;
        }

        if (args.length < 30 && !context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            context.reply(embedBuilder -> embedBuilder
                            .setColor(Color.RED)
                            .setDescription("Please provide a suggestion, it cannot be blank nor less than 30 characters!"),
                    15);
            return;
        }

        final TextChannel suggestionChannel = context.getGuild().getTextChannelById(suggestionId);
        if (suggestionChannel == null) {
            context.reply(embedBuilder -> embedBuilder
                            .setColor(Color.RED)
                            .setDescription("The suggestion channel could not be found with the configured ID."),
                    15);
            return;
        }

        if (!suggestionChannel.canTalk(context.getSelfMember())) {
            context.reply(embedBuilder -> embedBuilder
                            .setColor(Color.RED)
                            .setDescription("I cannot talk in the suggestion channel!"),
                    15);
            return;
        }

        final String suggestion = context.getMessage()
                .getContentRaw()
                .substring(Constants.PREFIX.length() + context.getInvoker().length());

        final EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(context.getAuthor().getAsTag(), null, context.getAuthor().getEffectiveAvatarUrl())
                .setDescription("**Suggestion:** " + suggestion)
                .setColor(0x34495e)
                .setTimestamp(Instant.now());

        suggestionChannel.sendMessage(embedBuilder.build()).queue(
                message -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                }
        );
    }

}
