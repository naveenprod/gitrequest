package com.gitrequest.rest.bohelper;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gitrequest.rest.bo.GitData;
import com.gitrequest.rest.dao.daoimpl.GitDaoImpl;

public class GitDataBoImpl implements GitDataBo{
	
	private static GitDataBoImpl s_instance = new GitDataBoImpl();
	public static GitDataBoImpl getInstance() {
			return s_instance;
	}
	@Override
	public boolean saveGitPullRequest(List<GitData> gitDataList){
		return GitDaoImpl.getInstance().storeGitPullData(gitDataList);
	}

	@Override
	public String fetchGitStateData() {
		String res = GitDaoImpl.getInstance().fetchGitStateData();
		JSONArray  resObj = new JSONArray("["+res+"]");
		return resObj.toString();
	}
}
