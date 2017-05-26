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
import com.tweetDownload.utils.GeneralConstants;

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterSearchService {

	@Value("${tweet.OAuthConsumerKey}")
	private String oAuthConsumerKey;

	@Value("${tweet.OAuthConsumerSecret}")
	private String oAuthConsumerSecret;

	@Value("${tweet.OAuthAccessToken}")
	private String oAuthAccessToken;

	@Value("${tweet.OAuthAccessTokenSecret}")
	private String oAuthAccessTokenSecret;

	@Value("${tweet.output.file.directory}")
	private String outputFileDirectory;

	@Value("${tweet.user.screenName}")
	private String screenName;
	
	final static Logger logger = Logger.getLogger(TwitterSearchService.class);

	public void stalkAUser() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(oAuthConsumerKey).setOAuthConsumerSecret(oAuthConsumerSecret)
				.setOAuthAccessToken(oAuthAccessToken).setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		cb.setJSONStoreEnabled(true);
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		int globalCount = 0;
		List<Status> statuses = new ArrayList<Status>();
		boolean doBreak = false;
		int pageno = 1;
		while (globalCount <= GeneralConstants.MAX_NUM_TWEETS) {
			while (true) {
				try {
					Paging page = new Paging(pageno++, 200);
					statuses.addAll(twitter.getUserTimeline(screenName, page));
					globalCount = statuses.size();
					if (statuses.size() >= GeneralConstants.MAX_NUM_TWEETS) {
						break;
					}
				} catch (Exception e) {
					logger.error("", e);
					doBreak = true;
					break;
				}

			}
			if(doBreak){
				break;
			}
		}
		File tweetFile = new File(outputFileDirectory+screenName);
		for(Status tweet : statuses){
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

}
