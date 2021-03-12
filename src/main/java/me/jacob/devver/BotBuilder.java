package me.jacob.devver;

import me.jacob.devver.event.GuildEvent;
import me.jacob.devver.policy.CustomCachePolicy;
import me.jacob.devver.utility.Checks;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;

public class BotBuilder {

	private final Config config;
	private JDA jda;

	public BotBuilder(Config config) {
		this.config = config;
	}

	public void buildDevver() throws LoginException, InterruptedException {
		final String token = config.getBuiltInstance().getString("token");
		Checks.notEmptyOrNull(token, "token");

		this.jda = JDABuilder.create(token, List.of(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES))
				.disableCache(List.of(CacheFlag.values()))
				.setMemberCachePolicy(new CustomCachePolicy(config))
				.setChunkingFilter(ChunkingFilter.include(config.getBuiltInstance().getLong("owner_id")))
				.addEventListeners(new GuildEvent(config))
				.build()
				.awaitReady();
	}

	public JDA getJda() {
		return jda;
	}
}
