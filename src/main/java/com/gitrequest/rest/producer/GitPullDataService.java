package com.gitrequest.rest.producer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.gitrequest.rest.bohelper.GitDataBoImpl;
import com.gitrequest.rest.exceptionhandler.ExceptionHelper;
import com.mongodb.util.JSON;

@Path("/git")
public class GitPullDataService {
	String gitPullData = "";
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/state")
	public String fetchState(){
		return ExceptionHelper.getResponse(GitDataBoImpl.getInstance().fetchGitStateData());		
	}
}
