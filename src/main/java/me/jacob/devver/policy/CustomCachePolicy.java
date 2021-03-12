package me.jacob.devver.policy;

import me.jacob.devver.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

public class CustomCachePolicy implements MemberCachePolicy {

	private final Config config;

	public CustomCachePolicy(Config config) {
		this.config = config;
	}

	@Override
	public boolean cacheMember(@NotNull Member member) {
		final long serverId = config.getBuiltInstance().getLong("server_id");
		final long ownerId = config.getBuiltInstance().getLong("owner_id");
		return (serverId != member.getGuild().getIdLong()) && (ownerId != member.getIdLong());
	}
}
