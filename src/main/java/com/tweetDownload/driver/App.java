package com.tweetDownload.driver;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tweetDownload.service.TweetCleanService;

public class App {
	 
	final static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring.xml");
		TweetCleanService cleanService = (TweetCleanService) context.getBean("tweetCleanService");
		try {
			cleanService.formatTweets();
		} catch (IOException e) {
			logger.error("",e);
		}
	}
}
