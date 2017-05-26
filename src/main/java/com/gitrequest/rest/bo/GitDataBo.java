package com.gitrequest.rest.bo;

import java.util.List;


public interface GitDataBo {

	public boolean saveGitPullRequest(List<GitData> gitDataList);
	public String fetchGitStateData();
}
