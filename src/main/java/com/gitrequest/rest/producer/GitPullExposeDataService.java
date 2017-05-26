package com.gitrequest.rest.producer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.gitrequest.rest.bohelper.GitDataBoImpl;
import com.gitrequest.rest.exceptionhandler.ExceptionHelper;

@Path("/git")
public class GitPullExposeDataService {
	String gitPullData = "";
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/state")
	public String fetchState(){
		return ExceptionHelper.getResponse(GitDataBoImpl.getInstance().fetchGitStateData());		
	}
}
