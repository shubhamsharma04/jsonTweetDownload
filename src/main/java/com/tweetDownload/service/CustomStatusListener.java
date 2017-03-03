package com.tweetDownload.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tweetDownload.dataformat.Tweet;
import com.tweetDownload.dataformat.User;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

@Service
public class CustomStatusListener implements StatusListener{
	
	@Value("${tweet.output.file.location}")
	private String outputFileLocation;
	
	final static Logger logger = Logger.getLogger(CustomStatusListener.class);

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		logger.info("Inside onDeletionNotice");
		logger.info(statusDeletionNotice.getStatusId());
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		logger.info("Inside onTrackLimitationNotice");
		logger.info(numberOfLimitedStatuses);
	}

	public void onException(Exception ex) {
		logger.error(ex);
		ex.printStackTrace();
	}

	public void onScrubGeo(long arg0, long arg1) {
		logger.info("Inside onScrubGeo");

	}

	public void onStallWarning(StallWarning arg0) {
		logger.info("Inside onStallWarning");
	}

	@Override
	public void onStatus(Status tweet) {
		File tweetFile = new File(outputFileLocation);
		// String tweetAsJson = TwitterObjectFactory.getRawJSON(status);
		Tweet tweetJson = new Tweet();
		if (tweet.isRetweet()) {
			tweetJson.setRetweet(true);
		}

		GeoLocation geoLocation = tweet.getGeoLocation();
		if (geoLocation != null) {
			double[] locations = new double[2];
			locations[1] = geoLocation.getLongitude();
			locations[0] = geoLocation.getLatitude();
			tweetJson.setTweet_loc(locations);
		}
		HashtagEntity[] hashTagEntities = tweet.getHashtagEntities();
		List<String> hashTagList = new ArrayList<String>();
		for (HashtagEntity hashTagEntity : hashTagEntities) {
			hashTagList.add(hashTagEntity.getText());
		}
		
		tweetJson.setHashtags(hashTagList);
		tweetJson.setTweet_text(tweet.getText());
		tweetJson.setTweet_lang(tweet.getLang());
		tweetJson.setTweet_date(String.valueOf(tweet.getCreatedAt()));
		URLEntity[] urlEntities = tweet.getURLEntities();
		List<String> urlList = new ArrayList<String>();
		for (URLEntity urlEntity : urlEntities) {
			urlList.add(urlEntity.getText());
		}
		tweetJson.setTweet_urls(urlList);
		UserMentionEntity[] userMentionEntities = tweet.getUserMentionEntities();
		List<String> mentionList = new ArrayList<String>();
		for (UserMentionEntity userMentionEntity : userMentionEntities) {
			mentionList.add(userMentionEntity.getText());
		}
		tweetJson.setMentions(mentionList);

		// Add User details
		twitter4j.User tweetUser = tweet.getUser();
		User user = new User();
		user.setScreenName(tweetUser.getScreenName());
		user.setFollowersCount(tweetUser.getFollowersCount());
		user.setFriendsCount(tweetUser.getFriendsCount());
		user.setLocation(tweetUser.getLocation());
		tweetJson.setUser(user);
		try {
			FileUtils.write(tweetFile, tweetJson.getAsJson(tweetJson) + "\n", StandardCharsets.UTF_8, true);
		} catch (IOException e) {
			logger.error(e);
		}

	}

}
