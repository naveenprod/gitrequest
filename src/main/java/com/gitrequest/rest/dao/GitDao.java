package com.gitrequest.rest.dao;

import java.util.List;

import com.gitrequest.rest.bo.GitData;
//Dao Interface
public interface GitDao {	
	public boolean storeGitPullData(List<GitData> user);
	public String fetchGitStateData();
}
