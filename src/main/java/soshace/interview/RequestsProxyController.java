package soshace.interview;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import vaks.com.dto.UserPost;
import vaks.com.dto.UserStat;

@RestController
public class RequestsProxyController {

	static final Logger logger = Logger.getLogger(RequestsProxyController.class.getName());
	
	List<UserPost> userPostList = new ArrayList<UserPost>();

	Map<Integer, UserStat> userStatMap = new TreeMap<Integer, UserStat>();

	public RequestsProxyController() {
		logger.log(Level.INFO,"RequestsProxyController ctor");
	}

	@PostConstruct
	public void init() {
		logger.log(Level.INFO,"init");
		// init user post list
		ObjectMapper mapper = new ObjectMapper();

		try {
			userPostList.addAll(Arrays
					.asList(mapper.readValue(new URL("http://jsonplaceholder.typicode.com/posts"), UserPost[].class)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(path = { "/posts/{postId}", "/posts" }, produces = "application/json")
	public List<UserPost> getPosts(@PathVariable(required = false) Integer postId,
			@CookieValue(name = "userId", required = false) Integer userId) {
		logger.log(Level.INFO,"requested posts");

		List<UserPost> filteredList = new ArrayList<UserPost>(userPostList);
		try {
			// filter if specific post requested

			if (postId == null) {
				// don't filter on postId
			} else {
				try {
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
			}

			// filter if specific user requested

			if (userId != null) {
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		return filteredList;
	}

	@RequestMapping(path = "/users", produces = "application/json")
	public Collection<UserStat> getUsers() {
		logger.log(Level.INFO,"requested users");
		return userStatMap.values();
	}

}