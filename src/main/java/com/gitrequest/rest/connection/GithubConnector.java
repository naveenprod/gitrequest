package com.gitrequest.rest.connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.kohsuke.github.*;

import io.deepstream.DeepstreamClient;
import io.deepstream.Record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

public class GithubConnector {

	private GitHub gitHub;
	private DeepstreamClient deepstreamClient = null;
	private String REPO_URL ="/GitRequest"+"/"+"rest"+"/"+"git"+"/"+"event";
	private  String deepstreamURI = "0.0.0.0:6020";
	private  String webhookURI = "http://da45aa06.ngrok.io";
	private  String repo = "naveenprod/gitrequest";

	/*public static void main(String[] args) throws IOException {
		new GithubConnector(deepstreamURI, webhookURI, repo);
	}*/

	public GithubConnector(String deepstreamURI, String webhookURI, String repo) throws IOException {
		try {
			deepstreamClient = new DeepstreamClient(deepstreamURI);
			deepstreamClient.login(new JsonObject());
		} catch (URISyntaxException e) {
			System.out.format("Malformed deepstream URI '%s'%n", deepstreamURI);
			throw new RuntimeException(e);
		}

		System.out.println("Connected to deepstream successfully");
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
	public List<GHPullRequest> getPullRequestData(GHRepository repository){
		List<GHPullRequest> gitpullList=null;
		try {
			for (GHHook hook : repository.getHooks()) {  // cleanup any pre-existing web hooks
				hook.delete();
			}
			startServer();								 // start the web hook listener
			List<GHEvent> events = Arrays.asList(GHEvent.PULL_REQUEST);  // subscribe to webhooks for Issue update events
			repository.createWebHook(new URL(webhookURI+REPO_URL), events);
			gitpullList = repository.getPullRequests(GHIssueState.ALL);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return gitpullList;
	}



	public void startServer() throws IOException {
		if(available(3000)){
			HttpServer server = HttpServer.create(new InetSocketAddress(3000), 0);
			server.createContext("/", new RequestHandler() );
			server.setExecutor(null);
			server.start();
		}
	}

	private static boolean available(int port) {
		try (Socket ignored = new Socket("localhost", port)) {
			return false;
		} catch (IOException ignored) {
			return true;
		}
	}
	class RequestHandler implements HttpHandler {

		public void handle(HttpExchange httpExchange) throws IOException {
			InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();
			String payload = URLDecoder.decode(query, "utf-8").replaceFirst("payload=", "");

			Gson gson = new Gson();
			JsonObject issueEvent = gson.fromJson(payload, JsonObject.class);

			String action = issueEvent.get("action").getAsString();

			JsonObject issue = issueEvent.get("issue").getAsJsonObject();
			String issueId = issue.get("id").getAsString();

			if (action.equals("edited")){
				String issueTitle = issue.get("title").getAsString();
				String issueUrl = issue.get("html_url").getAsString();

				deepstreamClient.record.getRecord(issueId)
				.set("title", issueTitle)
				.set("url", issueUrl);
			} else if (action.equals("labeled")) {
				deepstreamClient.record
				.getList(issueEvent.get("label").getAsJsonObject().get("name").getAsString())
				.addEntry(issueId);
			} else if (action.equals("unlabeled")) {
				deepstreamClient.record
				.getList(issueEvent.get("label").getAsJsonObject().get("name").getAsString())
				.removeEntry(issueId);
			}

			httpExchange.sendResponseHeaders(200, 0);
			OutputStream os = httpExchange.getResponseBody();
			os.close();
		}
	}
}