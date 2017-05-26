package com.gitrequest.rest.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;

import com.gitrequest.rest.dao.daoimpl.GitDaoImpl;
import com.gitrequest.rest.exceptionhandler.ExceptionHelper;
import com.gitrequest.rest.util.Util;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DbConnection {

	private static Properties prop = null;
	private DbConnection(){}
	
	private static DbConnection m_instance = new DbConnection();
	public static DbConnection getInstance() {
		return m_instance;
	}

	
	public static DB getConnection(){
		MongoClient mongoClient=null;
		DB mongoDb=null;
		try {
			try {
				prop = new Util().readFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			mongoClient = new MongoClient( prop.getProperty("dbhost") ,Integer.parseInt(prop.getProperty("dbport")));
			mongoDb = mongoClient.getDB(prop.getProperty("dbname"));
		} catch (NumberFormatException | UnknownHostException e) {
			e.printStackTrace();
			return null;
		}		
		return mongoDb;
	}
	
	
}
