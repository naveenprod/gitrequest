package com.gitrequest.rest.bohelper;

import java.util.List;

import com.gitrequest.rest.bo.GitData;

public interface GitDataBo {

	public boolean saveGitPullRequest(List<GitData> gitDataList);
	public String fetchGitStateData();
}
