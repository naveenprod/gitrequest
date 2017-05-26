package com.gitrequest.rest.bo;

import java.util.List;

public class GitData {
	
	private int number;
	private String state;
	private String title;
	private String body;
	private String created_at;
	private String updated_at;
	private String closed_at;
	private	String merged_at;
	private List<GitData> gitData;
	
	private GitData(){};
	
	public static GitData getInstance(){
		return new GitData();
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getClosed_at() {
		return closed_at;
	}
	public void setClosed_at(String closed_at) {
		this.closed_at = closed_at;
	}
	public String getMerged_at() {
		return merged_at;
	}
	public void setMerged_at(String merged_at) {
		this.merged_at = merged_at;
	}

	public List<GitData> getUsers() {
		return gitData;
	}

	public void setUsers(List<GitData> gitData) {
		this.gitData = gitData;
	}
	
}
