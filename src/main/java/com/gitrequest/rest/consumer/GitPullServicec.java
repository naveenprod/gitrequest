package com.gitrequest.rest.consumer;

import java.util.ArrayList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gitrequest.rest.bo.GitData;
import com.gitrequest.rest.bohelper.GitDataBoImpl;
import com.gitrequest.rest.exceptionhandler.ExceptionHelper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


@Path("/consume")
public class GitPullService {

	private  String GIT_PULL_URL="https://api.github.com/repos/hmkcode/java/pulls?state=all";
	private  List<GitData> gitDataList= new ArrayList<GitData>();
	private String output="No Data";

	@GET
	@Consumes
	public String consumeGitPullRequest() {
		try {
			Client client = Client.create();
			WebResource webResource = client.resource(GIT_PULL_URL);
			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			output = response.getEntity(String.class);
			JSONArray arr = new JSONArray(output);
			for(Object obj : arr){
				JSONObject pullData = (JSONObject) obj;
				setJsonObject(pullData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExceptionHelper.getResponse(GitDataBoImpl.getInstance().saveGitPullRequest(gitDataList));
	}
	
	public void setJsonObject(JSONObject json){
		GitData gitData = GitData.getInstance();
		gitData.setBody(String.valueOf(json.get("body")));
		gitData.setState(String.valueOf(json.get("state")));
		gitData.setNumber(json.getInt("number"));
		gitData.setTitle(json.getString("title"));
		gitData.setUpdated_at(String.valueOf(json.get("updated_at")));
		gitData.setMerged_at(String.valueOf(json.get("merged_at")));
		gitData.setCreated_at(String.valueOf(json.get("created_at")));
		gitData.setClosed_at(String.valueOf(json.get("closed_at")));
		gitDataList.add(gitData);
	}

	
	
}