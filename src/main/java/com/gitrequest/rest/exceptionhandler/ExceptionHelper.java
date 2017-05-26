package com.gitrequest.rest.exceptionhandler;

import org.json.JSONObject;

public class ExceptionHelper {
	public static JSONObject response;
	
	public static String getResponse(String res){
		if(null!=res && !res.equals("")){
			response = new JSONObject("{Data:"+res+", Response : '200 Ok'}");
		}else{
			response = new JSONObject("{Data:'No Data', Response:'404 Not Found'}");
		}
		return response.toString();
	}
	
	public static String getResponse(boolean res){
		String resultMsg ="";
		if(res){
			resultMsg = "Data Saved";
			response = new JSONObject("{Data:"+resultMsg+", Response : '201 Ok'}");
		}else{
			resultMsg ="Serivce Unavailable";
			response = new JSONObject("{Data:"+resultMsg+", Response:'404 Not Found'}");
		}
		return response.toString();
	}
	
	
	

}
