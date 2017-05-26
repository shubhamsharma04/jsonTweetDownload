package com.tweetDownload.driver;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tweetDownload.service.TwitterStreamingService;

public class App {
	 
	final static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring.xml");
		TwitterStreamingService twitterStreamingService = (TwitterStreamingService) context.getBean("twitterStreamingService");
		twitterStreamingService.streamTweets();
	}
}
