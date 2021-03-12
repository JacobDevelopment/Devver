package me.jacob.devver;

public class Devver {


	public static void main(String[] args) {
		try {
			new BotBuilder(new Config("config.json")).buildDevver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
