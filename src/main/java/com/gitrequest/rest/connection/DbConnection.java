package com.gitrequest.rest.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;

import com.gitrequest.rest.dao.daoimpl.GitDaoImpl;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DbConnection {

	private static String FILENAME = "connection.properties";
	private static Properties prop = new Properties();
	private DbConnection(){}
	
	private static DbConnection m_instance = new DbConnection();
	public static DbConnection getInstance() {
		return m_instance;
	}

	public Properties readFile() throws IOException{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(FILENAME).getFile());
		InputStream in = new FileInputStream(file);
		prop.load(in);
		return prop;
		
	}
	public static DB getConnection(){
		MongoClient mongoClient=null;
		try {
			mongoClient = new MongoClient( prop.getProperty("dbhost") ,Integer.parseInt(prop.getProperty("dbport")));
		} catch (NumberFormatException | UnknownHostException e) {
			e.printStackTrace();
		}
		DB mongoDb = mongoClient.getDB(prop.getProperty("dbname"));
		return mongoDb;
	}
	
	
}
