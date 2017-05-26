package com.gitrequest.rest.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import com.gitrequest.rest.bo.GitData;
import com.gitrequest.rest.bohelper.GitDataBoImpl;
import com.gitrequest.rest.connection.GithubConnector;
import com.gitrequest.rest.exceptionhandler.ExceptionHelper;

@Path("/git/event")
public class EventHandler {
	String DEEPSTREAM_URI="0.0.0.0:6020";
	String WEBHOOK_URI="http://285fc67f.ngrok.io";
	String GITHUB_REPO="naveenprod/gitrequest";
	private  List<GitData> gitDataList= new ArrayList<GitData>();

	@POST
	@Produces
	public String getGitEvent(){
		GithubConnector githubConnector = GithubConnector.getInstance(DEEPSTREAM_URI, WEBHOOK_URI, GITHUB_REPO);
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
