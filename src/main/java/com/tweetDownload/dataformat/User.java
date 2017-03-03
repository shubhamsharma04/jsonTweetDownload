package com.tweetDownload.dataformat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

	@JsonProperty("location")
	private String location;

	@JsonProperty("followers_count")
	private int followersCount;

	@JsonProperty("friends_count")
	private int friendsCount;

	@JsonProperty("screen_name")
	private String screenName;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}
