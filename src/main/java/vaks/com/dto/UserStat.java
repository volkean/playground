package vaks.com.dto;
import java.util.Date;

public class UserStat {
	/*
	 * "userId": 1, "requests": 1, "createdAt": "timestamp"
	 */

	int id;
	int requests;
	Date createdAt;

	public UserStat() {
	}

	public UserStat(int id, int requests, Date createdAt) {
		super();
		this.id = id;
		this.requests = requests;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRequests() {
		return requests;
	}

	public void setRequests(int requests) {
		this.requests = requests;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
