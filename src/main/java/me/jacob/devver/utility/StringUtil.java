package me.jacob.devver.utility;

public final class StringUtil {

	public static String linkMarkdown(String name, String url) {
		return String.format("[%s](%s)", name, url);
	}

}
