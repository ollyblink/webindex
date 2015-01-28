package main;

import twitter.TwitterBuilder;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import db.AbstractDBConnector;
import db.PGDBConnector;
import db.TwitterStreamDBCreatorAndWriter;

/**
 * Example of using the non-streaming API
 * 
 * DO NOT USE!
 * 
 * @author rsp
 *
 */
public class TwitterNonStreamingMain {
	/**
	 * Demonstrates twitter querying
	 *
	 * @param args
	 * @throws TwitterException
	 */
	public static void main(String[] args) throws TwitterException {
		// Database things
		String host = "localhost";
		String port = "5432";
		String database = "postgres";
		String user = "postgres";
		String password = "";
		AbstractDBConnector postgresConnector = new PGDBConnector(host, port, database, user, password);
		TwitterStreamDBCreatorAndWriter postgresIndex = new TwitterStreamDBCreatorAndWriter(postgresConnector, false);
		// End DB Things

		// Creating the twitterer
		Twitter twitter = TwitterBuilder.INSTANCE.newInstance();

		// The queries to search tweets for
		String[] queryWords = { "Crimea", "Putin", "Russia", "Ukraine" };
		// Always call insertQueries before calling insertStreamTweet, because
		// else, the queries are not yet present in the database
		postgresIndex.insertQueries(queryWords);
		// The number of tweets to retrieve (don't know if you can get more than
		// that at once with a free account...)
		int queryCount = 100;

		// Searching for the queries and writing the results to the postgresql
		// db...
		for (String queryWord : queryWords) {
			// Preparing the query
			Query query = new Query(queryWord);

			// max # of tweets per page
			query.setCount(queryCount);

			// Query twitter and get the tweets for the query
			QueryResult results = twitter.search(query);

			// Printing everything to the console
			for (Status status : results.getTweets()) {
				System.out.println("@" + status.getUser().getScreenName() + " location: " + status.getGeoLocation() + " - " + status.getText());
				System.out.println(status.getUser().getId());
				 postgresIndex.insertStreamTweet(status, queryWords);
 
			}
		}
	}
}
