package com.tweetDownload.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterStreamingService {

	@Value("${tweet.language}")
	private String[] languages;

	@Value("${tweet.search.keywords}")
	private String[] searchKeywords;

	@Value("${tweet.OAuthConsumerKey}")
	private String oAuthConsumerKey;

	@Value("${tweet.OAuthConsumerSecret}")
	private String oAuthConsumerSecret;

	@Value("${tweet.OAuthAccessToken}")
	private String oAuthAccessToken;

	@Value("${tweet.OAuthAccessTokenSecret}")
	private String oAuthAccessTokenSecret;

	@Value("${tweet.output.file.location}")
	private String outputFileLocation;
	
	@Autowired
	StatusListener listener;

	final static Logger logger = Logger.getLogger(TwitterStreamingService.class);

	public void twitterStreamingService() {

		/*StatusListener listener = new StatusListener() {
			File tweetFile = new File(outputFileLocation);

			public void onStatus(Status status) {
				//DataObjectFactory.getRawJSON(status);
				String json = TwitterObjectFactory.getRawJSON(status);
				System.out.println(json);
				logger.debug("Inside onStatus");
				Status tweet = status;
				if (tweet.isRetweet()) {
					logger.debug("Retweet");
					return;
				}
				Tweet tweetJson = new Tweet();
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
				tweetJson.setTopic("politics");
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
				try {
					FileUtils.write(tweetFile, tweetJson.getAsJson(tweetJson) + "\n", StandardCharsets.UTF_8, true);
				} catch (IOException e) {
					logger.error(e);
				}

			}

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
		}; */
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(oAuthConsumerKey).setOAuthConsumerSecret(oAuthConsumerSecret)
				.setOAuthAccessToken(oAuthAccessToken).setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
		 cb.setJSONStoreEnabled(true);
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(listener);
		FilterQuery filterQ = new FilterQuery();
		filterQ.language(languages);
		filterQ.track(searchKeywords);
		twitterStream.filter(filterQ);
	}

}
