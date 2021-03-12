package me.jacob.devver;

import net.dv8tion.jda.api.utils.data.DataObject;

import javax.xml.xpath.XPath;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Config {

	private final String path;

	private final DataObject builtInstance;

	public Config(String path) throws IOException {
		this.path = path;
		this.builtInstance = getInstance();
	}

	private DataObject getInstance() throws IOException {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
			return DataObject.fromJson(bufferedReader);
		}
	}

	public DataObject getBuiltInstance() {
		return builtInstance;
	}
}
