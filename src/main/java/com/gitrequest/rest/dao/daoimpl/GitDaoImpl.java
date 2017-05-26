package com.gitrequest.rest.dao.daoimpl;

import java.io.IOException;

import java.util.List;
import java.util.Properties;

import com.gitrequest.rest.connection.DbConnection;
import com.gitrequest.rest.bo.GitData;
import com.gitrequest.rest.dao.GitDao;
import com.gitrequest.rest.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;


public class GitDaoImpl implements GitDao{

	private static GitDaoImpl m_instance = new GitDaoImpl();
	public static GitDaoImpl getInstance() {
		return m_instance;
	}
	public boolean storeGitPullData(List<GitData> gitDataList) {
		Properties prop=null;
		boolean isInserted=false;
		try {
			prop = new Util().readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DB db = DbConnection.getConnection();
		if(null!=db){
			DBCollection dbCollection = db.getCollection(prop.getProperty("collectiongitpull"));
			if(null!=dbCollection){
				isInserted = true;
				for(GitData data : gitDataList){
					BasicDBObject docDetails = new BasicDBObject();
					docDetails.put("number", data.getNumber());
					docDetails.put("state", data.getState());
					docDetails.put("title",data.getTitle());
					docDetails.put("created_at",data.getCreated_at());
					docDetails.put("updated_at", data.getUpdated_at());
					docDetails.put("closed_at",data.getClosed_at());
					docDetails.put("merged_at", data.getMerged_at());
					DbConnection.getConnection().setWriteConcern(WriteConcern.SAFE);
					dbCollection.insert(docDetails);
				}
			}
		}return isInserted;
	}
	@Override
	public String fetchGitStateData() {
		Properties prop=null;
		String resultData="";
		try {
			prop = new Util().readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DB db = DbConnection.getConnection();
		if(null!=db){
			DBCollection dbCollection = db.getCollection(prop.getProperty("collectiongitpull"));
			DBCursor cursor = dbCollection.find();
			while(cursor.hasNext()){
				resultData=resultData+cursor.next()+",";
			}
		}
		return resultData;
	}



}
