package me.jacob.devver.command;

import net.dv8tion.jda.api.Permission;

import java.util.List;

public abstract class Command {

	private final String name;
	private final String description;

	private final String[] aliases;
	private Permission permission;
	private Permission[] permissions;

	public Command(String name, String description) {
		this.name = name;
		this.description = description;
		this.aliases = null;
		this.permissions = new Permission[]{Permission.MESSAGE_WRITE};
	}

	public Command(String name, String description, String[] aliases, Permission permission) {
		this.name = name;
		this.description = description;
		this.aliases = aliases;
		this.permission = permission;
	}

	public Command(String name, String description, String[] aliases, Permission... permissions) {
		this.name = name;
		this.description = description;
		this.aliases = aliases;
		this.permissions = permissions;
	}


	public abstract void run(CommandContext context, String[] args);

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String[] getAliases() {
		return aliases;
	}

	public Permission[] getPermissions() {
		return permissions;
	}

	public Permission getPermission() {
		return permission;
	}
}
