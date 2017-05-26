package com.gitrequest.rest.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;

import com.gitrequest.rest.bo.GitData;
import com.gitrequest.rest.bohelper.GitDataBoImpl;
import com.gitrequest.rest.connection.GithubConnector;

@Path("/git/event")
public class EventHandler {
	String DEEPSTREAM_URI="0.0.0.0:6020";
	String WEBHOOK_URI="http://4d00a551.ngrok.io";
	String GITHUB_REPO="naveenprod/gitrequest";
	private  List<GitData> gitDataList= new ArrayList<GitData>();
	
	@POST
	public void getGitEvent() throws IOException{
			
		  GithubConnector githubConnector = new GithubConnector(DEEPSTREAM_URI, WEBHOOK_URI, GITHUB_REPO);
		  GHRepository repository = githubConnector.conectToGitHub();
		  List<GHPullRequest> gitpullList = githubConnector.getPullRequestData(repository);
		  if(null!=gitpullList){
			  	for(GHPullRequest gitPull : gitpullList){
			  		GitData data = GitData.getInstance();
			  		data.setBody(gitPull.getBody());
			  		data.setNumber(gitPull.getNumber());
			  		data.setTitle(gitPull.getTitle());
			  		data.setState(gitPull.getState().toString());
			  		data.setCreated_at(gitPull.getCreatedAt().toString());
			  		data.setClosed_at(gitPull.getClosedAt().toString());
			  		data.setMerged_at(gitPull.getMergedAt().toString());
			  		gitDataList.add(data);
			  	}
			  	GitDataBoImpl.getInstance().saveGitPullRequest(gitDataList);
		  }
		 
		 
	}
	  
}
