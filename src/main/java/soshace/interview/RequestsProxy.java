package soshace.interview;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.Headers;
import vaks.com.UserPost;
import vaks.com.UserStat;

public class RequestsProxy {

	static Map<Integer, UserStat> userStatMap = new TreeMap<Integer, UserStat>();
	static List<UserPost> userPostList = null;

	public static void main(String args[]) throws Exception {
		RequestsProxy requestsProxy = new RequestsProxy();
		requestsProxy.serve();
	}

	public void serve() throws Exception {

		// init user post list
		ObjectMapper mapper = new ObjectMapper();
		userPostList = Arrays
				.asList(mapper.readValue(new URL("http://jsonplaceholder.typicode.com/posts"), UserPost[].class));

		Undertow server = Undertow.builder().addHttpListener(8080, "0.0.0.0").setHandler(new HttpHandler() {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {

				String[] requestPath = exchange.getRequestPath().split("/");
				if (requestPath.length == 2 && requestPath[1].equals("posts")) {
					System.out.println("requested all posts");
					handleGetAllPosts(exchange);
				} else if (requestPath.length == 3 && requestPath[1].equals("posts")) {
					System.out.println("requested postId=" + requestPath[2]);
					handleGetPostById(exchange, requestPath[2]);
				} else if (requestPath.length == 2 && requestPath[1].equals("users")) {
					System.out.println("requested users");
					handleGetUsers(exchange);
				} else if (requestPath.length == 2 && requestPath[1].equals("favicon.ico")) {
					System.out.println("requested favicon.ico");
					exchange.setStatusCode(404);
				} else {
					System.out.println("requested unknown url, defaulting to hello");
					exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
					exchange.getResponseSender().send("<html><body><h1>Hello</h1></body></<html>");
				}
			}
		}).build();
		server.start();
	}

	protected void handleGetUsers(HttpServerExchange exchange) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender()
					.send(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userStatMap.values()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void handleGetPostById(HttpServerExchange exchange, String postIdStr) {
		ObjectMapper mapper = new ObjectMapper();

		List<UserPost> filteredList = new ArrayList<UserPost>(userPostList);

		try {
			Integer postId = Integer.parseInt(postIdStr);
			// filter on postId
			filteredList = filteredList.stream().filter(t -> {
				if (postId == t.getId()) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());

		} catch (NumberFormatException e) {
			e.printStackTrace();
			// return none
			filteredList.clear();
		}

		// filter if specific user requested

		Cookie requestCookieUserId = exchange.getRequestCookie("userId");
		if (requestCookieUserId != null) {
			String userIdStr = requestCookieUserId.getValue();
			Integer userId = Integer.valueOf(userIdStr);
			UserStat userStat = userStatMap.get(userId);
			if (userStat == null) {
				userStatMap.put(userId, new UserStat(userId, 1, new Date()));
			} else {
				userStat.setRequests(userStat.getRequests() + 1);
				userStatMap.put(userId, userStat);
			}
			filteredList = filteredList.stream().filter(t -> {
				if (t.getUserId() == userId) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());

			// set visited
			filteredList.forEach(t -> t.setVisited(true));
		}

		try {
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredList));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void handleGetAllPosts(HttpServerExchange exchange) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		List<UserPost> filteredList = new ArrayList<UserPost>(userPostList);
		try {
			// filter if specific post requested

			AtomicInteger postId = new AtomicInteger(0);
			String postIdStr = StringUtils.substringAfter(exchange.getRequestPath(), "/posts/");
			if (postIdStr.isEmpty()) {
				// don't filter on postId
			} else {
				try {
					postId.set(Integer.parseInt(postIdStr));
					// filter on postId
					filteredList = filteredList.stream().filter(t -> {
						if (postId.get() == t.getId()) {
							return true;
						}
						return false;
					}).collect(Collectors.toList());

				} catch (NumberFormatException e) {
					e.printStackTrace();
					// return none
					filteredList.clear();
				}
			}

			// filter if specific user requested

			Cookie requestCookieUserId = exchange.getRequestCookie("userId");
			if (requestCookieUserId != null) {
				String userIdStr = requestCookieUserId.getValue();
				Integer userId = Integer.valueOf(userIdStr);
				UserStat userStat = userStatMap.get(userId);
				if (userStat == null) {
					userStatMap.put(userId, new UserStat(userId, 1, new Date()));
				} else {
					userStat.setRequests(userStat.getRequests() + 1);
					userStatMap.put(userId, userStat);
				}
				filteredList = filteredList.stream().filter(t -> {
					if (t.getUserId() == userId) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());

				// set visited
				filteredList.forEach(t -> t.setVisited(true));
			}

			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filteredList));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
