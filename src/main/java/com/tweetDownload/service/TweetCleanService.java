package com.tweetDownload.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetDownload.dataformat.Tweet;

@Service
public class TweetCleanService {

	@Value("${tweet.output.file.directory}")
	private String tweetRepo;

	@Value("${tweet.format.output.file.directory}")
	private String formattedTweetOutputLocation;

	@Value("${format.emoticons.file.location}")
	private String emoticonsFile;

	@Value("${format.kaomojis.file.location}")
	private String kaomojisFile;

	final static Logger logger = Logger.getLogger(TweetCleanService.class);

	public void formatTweets() throws IOException {

		List<String> allEmoticons = FileUtils.readLines(new File(emoticonsFile), StandardCharsets.UTF_8);
		List<String> allKaomojis = FileUtils.readLines(new File(kaomojisFile), StandardCharsets.UTF_8);

		Collection<File> allFiles = FileUtils.listFiles(new File(tweetRepo), null, false);
		for (File tweetFile : allFiles) {
			File outputFile = new File(formattedTweetOutputLocation + tweetFile.getName());
			FileUtils.write(outputFile, "", StandardCharsets.UTF_8, false);

			List<String> allTweets = FileUtils.readLines(tweetFile, StandardCharsets.UTF_8);
			List<String> outputTweets = new ArrayList<String>();
			ObjectMapper objectMapper = new ObjectMapper();
			for (String tweetObj : allTweets) {
				
				String tweet = objectMapper.readValue(tweetObj, Tweet.class).getTweet_text();

				for (String emoticon : allEmoticons) {
					if (tweet.contains(emoticon)) {
						try {
							if (emoticon.contains(")")) {
								tweet = tweet.replaceAll(emoticon.replaceAll("\\)", "\\\\)"), "");
							} else if (emoticon.contains("}")) {
								tweet = tweet.replaceAll(emoticon.replaceAll("\\}", "\\\\}"), "");
							}

							else if (emoticon.contains("(")) {
								tweet = tweet.replaceAll(emoticon.replaceAll("\\(", "\\\\("), "");
							} else if (emoticon.contains("{")) {
								tweet = tweet.replaceAll(emoticon.replaceAll("\\{", "\\\\{"), "");
							} else if (emoticon.contains("[")) {
								tweet = tweet.replaceAll(emoticon.replaceAll("\\[", "\\\\["), "");
							} else if (emoticon.contains("]")) {
								tweet = tweet.replaceAll(emoticon.replaceAll("\\]", "\\\\]"), "");
							}

							else {
								tweet = tweet.replaceAll(emoticon, "");
							}
						} catch (Exception e) {
							// I still find patterns in tweets which give all
							// sorts of exception
							logger.warn("tweet : " + tweet + " has a problematic pattern");
							logger.warn("", e);
							continue;
						}
					}
				}

				for (String kaomoji : allKaomojis) {
					if (tweet.contains(kaomoji)) {
						try {
							if (kaomoji.contains("(") && kaomoji.contains("")) {
								String repKaomoji = kaomoji.replaceAll("\\)", "\\\\)");
								repKaomoji = repKaomoji.replaceAll("\\(", "\\\\(");
								tweet = tweet.replaceAll(repKaomoji, "");
							} else if (kaomoji.contains(")")) {
								try {
									tweet = tweet.replaceAll(kaomoji.replaceAll("\\)", "\\\\)"), "");
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else if (kaomoji.contains("}")) {
								tweet = tweet.replaceAll(kaomoji.replaceAll("\\}", "\\\\}"), "");
							} else if (kaomoji.contains("(")) {
								tweet = tweet.replaceAll(kaomoji.replaceAll("\\(", "\\\\("), "");
							} else if (kaomoji.contains("{")) {
								tweet = tweet.replaceAll(kaomoji.replaceAll("\\{", "\\\\{"), "");
							} else if (kaomoji.contains("[")) {
								tweet = tweet.replaceAll(kaomoji.replaceAll("\\[", "\\\\["), "");
							} else if (kaomoji.contains("]")) {
								tweet = tweet.replaceAll(kaomoji.replaceAll("\\]", "\\\\]"), "");
							} else {
								tweet = tweet.replaceAll(kaomoji, "");
							}
						} catch (Exception e) {
							// I still find patterns in tweets which give all
							// sorts of exception
							logger.warn("tweet : " + tweet + " has a problematic pattern");
							logger.warn("", e);
							continue;
						}
					}
				}

				tweet = tweet.replaceAll("[^a-zA-Z\\s]", "");
				tweet = tweet.replaceAll("\\s{2,}", " ");
				outputTweets.add(tweet);
			}
			FileUtils.writeLines(outputFile, outputTweets, false);
		}
	}

}
