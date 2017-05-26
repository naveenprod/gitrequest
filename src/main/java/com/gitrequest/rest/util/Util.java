package com.gitrequest.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Util {
	private static Properties prop = new Properties();
	private static String FILENAME = "connection.properties";
	private static Util m_instance = new Util();
	public static Util getInstance() {
		return m_instance;
	}

	public Properties readFile() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(FILENAME).getFile());
		InputStream in = new FileInputStream(file);
		prop.load(in);
		return prop;
	}

}
