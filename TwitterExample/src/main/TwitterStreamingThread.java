package main;

import java.util.concurrent.BlockingQueue;

import db.TwitterStreamDBCreatorAndWriter;
import twitter.PGWriterListener;
import twitter.TwitterBuilder;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;

public class TwitterStreamingThread extends Thread {
	private static final String twitterCredentialsPropertyFileLocation = System.getProperty("user.dir") + "/src/main/resources/twitter.properties";

	private String[] keywordsToFilterFor;
	private BlockingQueue<UserMentionTuple> mentionedUsers;
	private TwitterStreamDBCreatorAndWriter postgresIndex;
	private boolean isDisplayTweetsEnabled;
	private boolean mustHaveGeolocation;
	private volatile boolean bExit = false;
	private String[] languagesToFilter;

	public TwitterStreamingThread(String[] keywordsToFilterFor, 
			String[] languagesToFilter, 
			BlockingQueue<UserMentionTuple> mentionedUsers, 
			TwitterStreamDBCreatorAndWriter postgresIndex, 
			boolean isDisplayTweetsEnabled,
			boolean mustHaveGeolocation) {
		this.keywordsToFilterFor = keywordsToFilterFor;
		this.languagesToFilter = languagesToFilter;
		this.mentionedUsers = mentionedUsers;
		this.postgresIndex = postgresIndex;
		this.isDisplayTweetsEnabled = isDisplayTweetsEnabled;
		this.mustHaveGeolocation = mustHaveGeolocation;
	}

	@Override
	public void run() {
		FilterQuery fq = new FilterQuery();
		fq.track(keywordsToFilterFor);
		if(languagesToFilter != null && languagesToFilter.length > 0){
			fq.language(languagesToFilter);
		}
		// End query specifications

		// Creating a new twitter stream
		TwitterStream twitterStream = new TwitterStreamFactory(TwitterBuilder.INSTANCE.newConfigurationBuilder(twitterCredentialsPropertyFileLocation).build()).getInstance();

		// Creating a listener to that stream
		PGWriterListener listener = new PGWriterListener(mentionedUsers, postgresIndex, keywordsToFilterFor, isDisplayTweetsEnabled, mustHaveGeolocation);
		// Adding the stream listener
		twitterStream.addListener(listener);

		// Filtering the incoming stream for the specified query words
		twitterStream.filter(fq);
		while(!bExit){
			
		}
		twitterStream.removeListener(listener); 
		

	}

	public void exit(boolean bExit) {
		this.bExit = bExit;
	}
}
