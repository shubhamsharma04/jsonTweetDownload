package com.tweetDownload.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

@Service
public class CustomStatusListener implements StatusListener {

	@Value("${tweet.output.file.location}")
	private String outputFileLocation;

	private static List<Tweet> bufferedTweets = new ArrayList<Tweet>();

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see twitter4j.StatusListener#onStatus(twitter4j.Status) Takes the heavy
	 * status object, parses some relevant information from it and stores it in
	 * a local file
	 */
	@Override
	public void onStatus(Status tweet) {
		// If you don't mind storing the entire tweet json, uncomment the
		// following line and write it to the file
		// String tweetAsJson = TwitterObjectFactory.getRawJSON(status);
		Tweet miniTweet = new Tweet();
		if (tweet.isRetweet()) {
			miniTweet.setRetweet(true);
		}

		miniTweet.setId(tweet.getId());
		miniTweet.setTweet_text(tweet.getText());
		miniTweet.setTweet_lang(tweet.getLang());
		miniTweet.setTweet_date(String.valueOf(tweet.getCreatedAt()));

		setGeoLocation(miniTweet, tweet);

		HashtagEntity[] hashTagEntities = tweet.getHashtagEntities();
		setEntityCollection(miniTweet, Arrays.asList(hashTagEntities));

		URLEntity[] urlEntities = tweet.getURLEntities();
		setEntityCollection(miniTweet, Arrays.asList(urlEntities));

		UserMentionEntity[] userMentionEntities = tweet.getUserMentionEntities();
		setEntityCollection(miniTweet, Arrays.asList(userMentionEntities));

		// Add User details
		setUser(miniTweet, tweet);
		bufferOrStoreTweet(miniTweet);
	}

	private void setGeoLocation(Tweet miniTweet, Status tweet) {
		GeoLocation geoLocation = tweet.getGeoLocation();
		if (geoLocation != null) {
			double[] locations = new double[2];
			locations[1] = geoLocation.getLongitude();
			locations[0] = geoLocation.getLatitude();
			miniTweet.setTweet_loc(locations);
		}
	}

	private void setUser(Tweet miniTweet, Status tweet) {
		twitter4j.User tweetUser = tweet.getUser();
		User user = new User();
		user.setScreenName(tweetUser.getScreenName());
		user.setFollowersCount(tweetUser.getFollowersCount());
		user.setFriendsCount(tweetUser.getFriendsCount());
		user.setLocation(tweetUser.getLocation());
		miniTweet.setUser(user);
	}

	private void setEntityCollection(Tweet miniTweet, List<?> entities) {
		if (entities != null && entities.size() > 0) {
			Object classObject = entities.get(0);
			List<String> entityList = new ArrayList<String>();
			if (HashtagEntity.class.isInstance(classObject)) {
				for (Object entity : entities) {
					entityList.add(((HashtagEntity) entity).getText());
				}
				miniTweet.setHashtags(entityList);
			} else if (URLEntity.class.isInstance(classObject)) {
				for (Object entity : entities) {
					entityList.add(((URLEntity) entity).getText());
				}
				miniTweet.setTweet_urls(entityList);
			} else if (UserMentionEntity.class.isInstance(classObject)) {
				for (Object entity : entities) {
					entityList.add(((UserMentionEntity) entity).getText());
				}
				miniTweet.setMentions(entityList);
			} else {
				logger.warn("Object : " + classObject.getClass() + " is incompatible with this method.");
			}

		}
	}

	private synchronized void bufferOrStoreTweet(Tweet miniTweet) {
		bufferedTweets.add(miniTweet);
		if (bufferedTweets.size() >= GeneralConstants.BUFFERED_TWEET_SIZE) {
			File tweetFile = new File(outputFileLocation);
			try {
				List<String> outputTweets = new ArrayList<String>();
				for(Tweet tweet : bufferedTweets){
					outputTweets.add(tweet.getAsJson(tweet));
				}
				FileUtils.writeLines(tweetFile, "UTF-8", (Collection<String>) outputTweets, "\n", true);
			} catch (IOException e) {
				logger.error(e);
			}
			bufferedTweets = new ArrayList<Tweet>();
		}
	}

}
