package com.gitrequest.rest.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import com.gitrequest.rest.bo.GitData;
import com.gitrequest.rest.bohelper.GitDataBoImpl;
import com.gitrequest.rest.connection.DbConnection;
import com.gitrequest.rest.connection.GithubConnector;
import com.gitrequest.rest.exceptionhandler.ExceptionHelper;
import com.gitrequest.rest.util.Util;

@Path("/git/event")
public class EventHandler {
	private  List<GitData> gitDataList= new ArrayList<GitData>();
	Properties prop=null;
	@POST
	@Produces
	public String getGitEvent(){
		try {
			prop = new Util().readFile();
		} catch (IOException e) {
			e.printStackTrace();
			return ExceptionHelper.getResponse(null);
		}
		GithubConnector githubConnector = GithubConnector.getInstance(prop.getProperty("DEEPSTREAM_URI"), prop.getProperty("WEBHOOK_URI"), prop.getProperty("GITHUB_REPO"));
		GHRepository repository = githubConnector.conectToGitHub();
		List<List<GHPullRequest>> allList = githubConnector.getPullRequestData(repository);
		if(null!=allList){
			for(List<GHPullRequest> gitList : allList){
				for(GHPullRequest gitPull : gitList){
					GitData data = GitData.getInstance();
					data.setBody(gitPull.getBody());
					data.setNumber(gitPull.getNumber());
					data.setTitle(gitPull.getTitle());
					data.setState(String.valueOf(gitPull.getState()));
					try {
						data.setCreated_at(String.valueOf(gitPull.getCreatedAt()));
					} catch (IOException e) {
						e.printStackTrace();
						return ExceptionHelper.internalServerError();
					}
					data.setClosed_at(String.valueOf(gitPull.getClosedAt()));
					data.setMerged_at(String.valueOf(gitPull.getMergedAt()));
					gitDataList.add(data);
				}
			}
		}
		return ExceptionHelper.getResponse(GitDataBoImpl.getInstance().saveGitPullRequest(gitDataList));
	}

}
