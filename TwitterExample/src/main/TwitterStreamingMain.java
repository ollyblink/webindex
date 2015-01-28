package main;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import twitter.TwitterBuilder;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import db.AbstractDBConnector;
import db.PGDBConnector;
import db.TwitterStreamDBCreatorAndWriter;

public final class TwitterStreamingMain {

	public static void main(String[] args) throws TwitterException {

		// Parameter configurationes
		String host = "localhost";
		String port = "5432";
		String database = "postgres";
		String user = "postgres";
		String password = "32qjivkd";
		String[] keywordsToFilterFor = { "sex" };
		String[] languages = new String[] { "en" };
//		int timeOut = 50000;
		boolean mustHaveGeolocation = true;
		boolean printIncomingTweets = true;

		// System initialisation
		AbstractDBConnector postgresConnector = new PGDBConnector(host, port, database, user, password);
		TwitterStreamDBCreatorAndWriter postgresIndex = new TwitterStreamDBCreatorAndWriter(postgresConnector, false);
		Twitter twitter = TwitterBuilder.INSTANCE.newInstance();
		BlockingQueue<UserMentionTuple> mentionedUsers = null;
		MentionedUserAdder consumer = null;
		/*
		 * IMPORTANT: THE NEXT THREE (3) LINES ARE !!OPTIONAL!! IF YOU DON'T
		 * WANT TO SAVE FOLLOWERS FOR EACH OF THE USERS THAT SUBMITTED A TWEET,
		 * COMMENT THESE TWO LINES OUT AND ONLY RUN TwitterStreamingRunnable,
		 * WITH FollowersAdder SET TO NULL!!!!!
		 */
		 mentionedUsers = new LinkedBlockingQueue<UserMentionTuple>(); //OPTIONAL
		 consumer = new MentionedUserAdder(mentionedUsers, postgresIndex,
		 twitter);// OPTIONAL
		 consumer.start();// OPTIONAL
		// END OPTIONAL PART

		TwitterStreamingThread producer = new TwitterStreamingThread(keywordsToFilterFor, languages, mentionedUsers, postgresIndex, printIncomingTweets, mustHaveGeolocation);
		producer.start();
		
		System.err.println("To stop streaming, please enter the word stop");
		Scanner scanner = new Scanner(System.in);
		
		if (scanner.next("stop").equals("stop")) {   
			producer.exit(true);
			if (mentionedUsers != null && consumer != null) {
				while (!mentionedUsers.isEmpty()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.err.println("still "+mentionedUsers.size() +" mentioned users to add.");
				}
				System.err.println("Before interrupting consumer.");
				consumer.exit(true);
			}
			System.err.println("Finished processing!");
			System.exit(0);
		}
		scanner.close();
	}
}