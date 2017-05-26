package com.gitrequest.rest.connection;

import io.deepstream.DeepstreamClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHHook;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class GithubConnector {

	private GitHub gitHub;
	private static DeepstreamClient deepstreamClient = null;
	private String REPO_URL ="/GitRequest"+"/"+"rest"+"/"+"git"+"/"+"event";
	private  String webhookURI = "http://285fc67f.ngrok.io";
	private  String repo = "naveenprod/gitrequest";
	private  boolean isServerStarted=false;
	private static GithubConnector instance=null;

	
	public static GithubConnector getInstance(String deepstreamURI, String webhookURI, String repo){
		try {
			if(instance==null){
				instance = new GithubConnector();
				deepstreamClient = new DeepstreamClient(deepstreamURI);
				deepstreamClient.login(new JsonObject());
				
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return instance;
	}

	// connect to github
	public GHRepository conectToGitHub(){
		GHRepository repository=null;
		try {
			gitHub = GitHub.connect();
			repository = gitHub.getRepository(repo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return repository;
	}
	public List<List<GHPullRequest>> getPullRequestData(GHRepository repository){
		List<List<GHPullRequest>> allList = new ArrayList<List<GHPullRequest>>();
		List<GHPullRequest> gitpullList=null;
		try {
			for (GHHook hook : repository.getHooks()) {  // cleanup any pre-existing web hooks
				hook.delete();
			}
			if(!isServerStarted){
				startServer();
			}
			List<GHEvent> events = Arrays.asList(GHEvent.PULL_REQUEST);  // subscribe to webhooks for pull update events
			repository.createWebHook(new URL(webhookURI+REPO_URL), events);
			gitpullList = repository.getPullRequests(GHIssueState.OPEN);
			allList.add(gitpullList);
			gitpullList = repository.getPullRequests(GHIssueState.CLOSED);
			allList.add(gitpullList);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return allList;
	}



	public void startServer() throws IOException {
			isServerStarted = true;
			HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
			server.createContext("/", new RequestHandler() );
			server.setExecutor(null);
			server.start();
		}

	/*private static boolean available(int port) {
		try (Socket ignored = new Socket("localhost", port)) {
			return false;
		} catch (IOException ignored) {
			return true;
		}
	}*/
	class RequestHandler implements HttpHandler {

		public void handle(HttpExchange httpExchange) throws IOException {
			httpExchange.sendResponseHeaders(200, 0);
			OutputStream os = httpExchange.getResponseBody();
			os.close();
		}
	}
}