package com.tweetDownload.dataformat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Tweet {

	@JsonProperty("topic")
	private String topic;

	@JsonProperty("tweet_text")
	private String tweet_text;

	@JsonProperty("tweet_lang")
	private String tweet_lang;

	@JsonProperty("text_en")
	private String text_en;

	@JsonProperty("text_ko")
	private String text_ko;

	@JsonProperty("text_es")
	private String text_es;

	@JsonProperty("text_tr")
	private String text_tr;

	@JsonProperty("hashtags")
	private List<String> hashtags;

	@JsonProperty("mentions")
	private List<String> mentions;

	@JsonProperty("tweet_urls")
	private List<String> tweet_urls;

	@JsonProperty("tweet_emoticons")
	private List<String> tweet_emoticons;

	@JsonProperty("tweet_date")
	private String tweet_date;

	@JsonProperty("tweet_loc")
	private double[] tweet_loc;
	
	@JsonProperty("isRetweet")
	private boolean isRetweet;

	public boolean isRetweet() {
		return isRetweet;
	}

	public void setRetweet(boolean isRetweet) {
		this.isRetweet = isRetweet;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTweet_text() {
		return tweet_text;
	}

	public void setTweet_text(String tweet_text) {
		this.tweet_text = tweet_text;
	}

	public String getTweet_lang() {
		return tweet_lang;
	}

	public void setTweet_lang(String tweet_lang) {
		this.tweet_lang = tweet_lang;
	}

	public String getText_en() {
		return text_en;
	}

	public void setText_en(String text_en) {
		this.text_en = text_en;
	}

	public String getText_ko() {
		return text_ko;
	}

	public void setText_ko(String text_ko) {
		this.text_ko = text_ko;
	}

	public String getText_es() {
		return text_es;
	}

	public void setText_es(String text_es) {
		this.text_es = text_es;
	}

	public String getText_tr() {
		return text_tr;
	}

	public void setText_tr(String text_tr) {
		this.text_tr = text_tr;
	}

	public List<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}

	public List<String> getMentions() {
		return mentions;
	}

	public void setMentions(List<String> mentions) {
		this.mentions = mentions;
	}

	public List<String> getTweet_urls() {
		return tweet_urls;
	}

	public void setTweet_urls(List<String> tweet_urls) {
		this.tweet_urls = tweet_urls;
	}

	public List<String> getTweet_emoticons() {
		return tweet_emoticons;
	}

	public void setTweet_emoticons(List<String> tweet_emoticons) {
		this.tweet_emoticons = tweet_emoticons;
	}

	public String getTweet_date() {
		return tweet_date;
	}

	public void setTweet_date(String date) {
		this.tweet_date = date;
	}

	public double[] getTweet_loc() {
		return tweet_loc;
	}

	public void setTweet_loc(double[] tweet_loc) {
		this.tweet_loc = tweet_loc;
	}

	@JsonIgnoreProperties
	public String getAsJson(Tweet tweet) {
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result = mapper.writeValueAsString(tweet);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
