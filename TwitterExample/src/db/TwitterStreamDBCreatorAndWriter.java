package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgis.PGgeometry;
import org.postgis.Point;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.User;

/**
 * Creates a spatial index based on PostgreSQL and PostGIS, and stores the images' coordinates in a GIST index.
 * 
 * @author Oliver Zihler
 * @version 0.1
 * 
 */
public final class TwitterStreamDBCreatorAndWriter {
	private static final String[] SQL_STATEMENTS = new String[] { 
		"create table if not exists users(id bigint primary key not null, screenname text, number_of_followers integer, createdAt date, location text, timezone text);", 
		"create table if not exists tweets(id bigint primary key not null, userid bigint, tweet_text text not null, created_at date, retweeted boolean, language text, source text);", "create table if not exists submits(userid bigint not null references users(id), mentioneduserid bigint references users(id), tweetid bigint not null references tweets(id), primary key(userid, mentioneduserid, tweetid));", "create table if not exists hashtags(hashtag text primary key not null);", "create index hashtags_hashtag_index on hashtags(hashtag);", "create table if not exists tweet_to_hashtags(tweet_id bigint not null references tweets(id), hashtag text not null references hashtags(hashtag), primary key(tweet_id, hashtag));",
			"create table if not exists queries(query text not null primary key);", 
			"create table tweet_to_queries(tweet_id bigint not null references tweets(id), query text not null references queries(query), primary key(tweet_id, query));", 
			"create index submits_user_index on submits(userid);", "create index submits_mentioneduserid_index on submits(mentioneduserid);", "create index submits_tweetid_index on submits(tweetid);", "create index tweets_userid_index on tweets(id);", "create index tweets_tweet_text_index on tweets(tweet_text);", "create index tweets_created_at_index on tweets(created_at);", "create index tweets_retweeted_index on tweets(retweeted);", "create index tweets_language_index on tweets(language);", "create index users_screenname_index on users(screenname);", "create index users_number_of_followers_index on users(number_of_followers);", "create index users_createdAt_index on users(createdAt);", "create index users_location_index on users(location);",
			"select addgeometrycolumn('tweets','geolocation','4326','geometry',2);", "create index tweets_spatial_index on tweets using gist(geolocation);" };

	private final AbstractDBConnector database;
	private final Connection connection;

	private boolean isPrintViolationsEnabled;

	public void dropTables() {
		String sql = "drop table if exists users, tweets, submits, hashtags, tweet_to_hashtags, queries, tweet_to_queries, terms cascade;";

		try {
			Statement s = database.getConnection().createStatement();

			s.execute(sql);
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private final Set<String> queries;
	// private final Set<String> hashtags;

	public TwitterStreamDBCreatorAndWriter(AbstractDBConnector database, boolean isPrintViolationsEnabled) {
		this.database = database;
		this.connection = database.getConnection();
		this.isPrintViolationsEnabled = isPrintViolationsEnabled;

		// try {
		// ((org.postgresql.PGConnection) connection).addDataType("geometry",
		// Class.forName("org.postgis.PGgeometry"));
		// } catch (ClassNotFoundException | SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/**
	 * Recreates an existing index before a new index is created.
	 */
	public void initializeTable() {

		try {
			Statement statement = connection.createStatement();
			for (String sql : SQL_STATEMENTS) {
				System.out.println(sql);
				statement.execute(sql);
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Always call insertQueries before calling insertStreamTweet, because else, the queries are not yet present in the database
	 * 
	 * @param queryWords
	 */
	public void insertQueries(String[] queryWords) {
		for (String query : queryWords) {
			updateQueries(query);
		}
	}

	/**
	 * Always call insertQueries before calling insertStreamTweet, because else, the queries are not yet present in the database
	 * 
	 * @param status
	 * @param queryWords
	 * @param followers
	 *            id's of the followers of a user
	 */
	public void insertStreamTweet(Status status, String[] queryWords) {

		for (HashtagEntity e : status.getHashtagEntities()) {
			updateHashTags(e);
		}

		updateUser(status.getUser());
		updateTweets(status);

		for (HashtagEntity e : status.getHashtagEntities()) {
			updateTweetsToHashtags(status, e);
		}

		for (String query : queryWords) {
			updateTweetsToQueries(status, query);
		}

	}

	//
	// /**
	// *
	// * @param userID
	// * the user that has a number of followers
	// * @param followerID
	// * the id of the follower of a user
	// */
	// public void updateFollower(long userID, long followerID, Timestamp followingAt) {
	// try {
	// PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from followers f where f.userid=? AND f.followerid=? AND f.following_at=?;");
	// checkIfAlreadyStored.setLong(1, userID);
	// checkIfAlreadyStored.setLong(2, followerID);
	// checkIfAlreadyStored.setTimestamp(3, followingAt);
	// ResultSet count = checkIfAlreadyStored.executeQuery();
	// int numberOfSameQuery = 0;
	// while (count.next()) {
	// numberOfSameQuery = count.getInt(1);
	// }
	// if (isPrintViolationsEnabled && numberOfSameQuery > 0) {
	// System.err.println("found one violation in followers");
	// }
	//
	// if (numberOfSameQuery == 0) {
	// PreparedStatement updateTweetsToQueries = connection.prepareStatement("Insert into followers values(?,?,?);");
	// updateTweetsToQueries.setLong(1, userID);
	// updateTweetsToQueries.setLong(2, followerID);
	// updateTweetsToQueries.setTimestamp(3, followingAt);
	// updateTweetsToQueries.executeUpdate();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	//
	// }

	private void updateTweetsToQueries(Status status, String query) {
		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from tweet_to_queries tq where tq.tweet_id=? AND tq.query=?");
			checkIfAlreadyStored.setLong(1, status.getId());
			checkIfAlreadyStored.setString(2, query);
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameQuery = 0;
			while (count.next()) {
				numberOfSameQuery = count.getInt(1);
			}
			if (isPrintViolationsEnabled && numberOfSameQuery > 0)
				System.err.println("found one violation in updateTweetsToQueries");

			if (numberOfSameQuery == 0) {
				PreparedStatement updateTweetsToQueries = connection.prepareStatement("Insert into tweet_to_queries values(?,?)");
				updateTweetsToQueries.setLong(1, status.getId());
				updateTweetsToQueries.setString(2, query);
				updateTweetsToQueries.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateTweetsToHashtags(Status status, HashtagEntity e) {
		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from tweet_to_hashtags th where th.tweet_id=? AND th.hashtag=?");
			checkIfAlreadyStored.setLong(1, status.getId());
			checkIfAlreadyStored.setString(2, e.getText());
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameQuery = 0;
			while (count.next()) {
				numberOfSameQuery = count.getInt(1);
			}
			if (isPrintViolationsEnabled && numberOfSameQuery > 0)
				System.err.println("found one violation in updateTweetsToHashtags");
			if (numberOfSameQuery == 0) {
				PreparedStatement updateTweetsToHashtags = connection.prepareStatement("Insert into tweet_to_hashtags values(?,?)");
				updateTweetsToHashtags.setLong(1, status.getId());
				updateTweetsToHashtags.setString(2, e.getText());
				updateTweetsToHashtags.executeUpdate();
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	private void updateTweets(Status status) {
		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from tweets where id=?");
			checkIfAlreadyStored.setLong(1, status.getId());
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameQuery = 0;
			while (count.next()) {
				numberOfSameQuery = count.getInt(1);
			}
			if (isPrintViolationsEnabled && numberOfSameQuery > 0)
				System.err.println("found one violation in updateTweets");
			if (numberOfSameQuery == 0) {
				User user = status.getUser();
				PreparedStatement updateTweets = connection.prepareStatement("Insert into tweets values(?,?,?,?,?,?,?,?)");
				updateTweets.setLong(1, status.getId());
				updateTweets.setLong(2, user.getId());
				updateTweets.setString(3, status.getText());
				if (status.getCreatedAt() != null) {
					updateTweets.setDate(4, new Date(status.getCreatedAt().getTime()));
				} else {
					updateTweets.setDate(4, null);
				}
				updateTweets.setBoolean(5, status.isRetweeted());
				updateTweets.setString(6, status.getLang());

				updateTweets.setString(7, status.getSource());
				if (status.getGeoLocation() != null) {
					Point point = new Point(status.getGeoLocation().getLongitude(), status.getGeoLocation().getLatitude());
					point.setSrid(4326);
					updateTweets.setObject(8, new PGgeometry(point));
				} else {
					updateTweets.setString(7, null);
					updateTweets.setObject(8, null);
				}
				updateTweets.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateUser(User user) {
		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from users where id=?");
			checkIfAlreadyStored.setLong(1, user.getId());
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameQuery = 0;
			while (count.next()) {
				numberOfSameQuery = count.getInt(1);
			}
			if (isPrintViolationsEnabled && numberOfSameQuery > 0)
				System.err.println("found one violation in updateUsers");
			if (numberOfSameQuery == 0) {
				PreparedStatement updateUsers = connection.prepareStatement("Insert into users values(?,?,?,?,?,?)");
				updateUsers.setLong(1, user.getId());
				updateUsers.setString(2, user.getScreenName());
				updateUsers.setInt(3, user.getFollowersCount());
				if (user.getCreatedAt() != null) {
					updateUsers.setDate(4, new Date(user.getCreatedAt().getTime()));
				} else {
					updateUsers.setDate(4, null);
				}
				updateUsers.setString(5, user.getLocation());
				updateUsers.setString(6, user.getTimeZone());

				updateUsers.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateHashTags(HashtagEntity e) {
		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from hashtags where hashtag=?");
			checkIfAlreadyStored.setString(1, e.getText());
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameQuery = 0;
			while (count.next()) {
				numberOfSameQuery = count.getInt(1);
			}
			if (isPrintViolationsEnabled && numberOfSameQuery > 0)
				System.err.println("found one violation in updateHashTags");
			if (numberOfSameQuery == 0) {
				PreparedStatement updateHashtags = connection.prepareStatement("Insert into hashtags values(?)");
				updateHashtags.setString(1, e.getText());
				updateHashtags.executeUpdate();
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	private void updateQueries(String query) {

		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from queries where query=?");
			checkIfAlreadyStored.setString(1, query);
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameQuery = 0;
			while (count.next()) {
				numberOfSameQuery = count.getInt(1);
			}
			if (isPrintViolationsEnabled && numberOfSameQuery > 0) {
				System.err.println("found one violation in updateQueries");
			}
			if (numberOfSameQuery == 0) {
				PreparedStatement updateQueries = connection.prepareStatement("Insert into queries values(?)");
				updateQueries.setString(1, query);
				updateQueries.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPrintViolationsEnabled(boolean isPrintViolationsEnabled) {
		this.isPrintViolationsEnabled = isPrintViolationsEnabled;
	}

	public void close() {
		database.closeConnection();
	}

	public boolean containsUser(long userID) {
		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from users where id=?");
			checkIfAlreadyStored.setLong(1, userID);
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameUser = 0;
			while (count.next()) {
				numberOfSameUser = count.getInt(1);
			}
			return numberOfSameUser > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updateSubmits(long userID, long mentionedUserID, long tweetID) {
		try {
			PreparedStatement checkIfAlreadyStored = connection.prepareStatement("Select count(*) from submits where userid=? AND mentioneduserid=? AND tweetid=?");
			checkIfAlreadyStored.setLong(1, userID);
			checkIfAlreadyStored.setLong(2, mentionedUserID);
			checkIfAlreadyStored.setLong(3, tweetID);
			ResultSet count = checkIfAlreadyStored.executeQuery();
			int numberOfSameQuery = 0;
			while (count.next()) {
				numberOfSameQuery = count.getInt(1);
			}
			if (isPrintViolationsEnabled && numberOfSameQuery > 0) {
				System.err.println("found one violation in updateQueries");
			}
			if (numberOfSameQuery == 0) {
				PreparedStatement updateQueries = connection.prepareStatement("Insert into submits values(?,?,?)");
				updateQueries.setLong(1, userID);
				updateQueries.setLong(2, mentionedUserID);
				updateQueries.setLong(3, tweetID);
				updateQueries.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.println(userID + ", " + mentionedUserID + ", " + tweetID);
			e.printStackTrace();
		}

	}

}
