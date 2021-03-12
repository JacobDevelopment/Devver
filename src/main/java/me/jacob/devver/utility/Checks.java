package me.jacob.devver.utility;

public class Checks {

	public static void notEmptyOrNull(String string, String name) {
		if (string == null || string.isEmpty())
			throw new IllegalStateException(name + " was null or empty!");
	}

	public static void notNull(Object object, String name) {
		if (object == null)
			throw new IllegalStateException(name + " is null!");
	}

}
