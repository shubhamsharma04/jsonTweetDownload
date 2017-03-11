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
