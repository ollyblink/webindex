package main;

import java.util.concurrent.BlockingQueue;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import db.TwitterStreamDBCreatorAndWriter;

public class MentionedUserAdder extends Thread {
	private BlockingQueue<UserMentionTuple> mentionedUsersToAdd;
	private TwitterStreamDBCreatorAndWriter postgresIndex;
	private Twitter twitter;

	private volatile boolean bExit = false;
	private boolean isPrintingEnabled;

	public MentionedUserAdder(BlockingQueue<UserMentionTuple> mentionedUsersToAdd, TwitterStreamDBCreatorAndWriter postgresIndex, Twitter twitter) {

		this.mentionedUsersToAdd = mentionedUsersToAdd;
		this.postgresIndex = postgresIndex;
		this.twitter = twitter;
	}

	public void exit(boolean bExit) {
		this.bExit = bExit;
	}

	@Override
	public void run() {
		while (!bExit) {
			try {
				RateLimitStatus status = twitter.getRateLimitStatus("users").get("/users/show/:id");
				int toTake = status.getRemaining();

				while (true) {
					if (toTake > 0) {  
						UserMentionTuple mentioned = mentionedUsersToAdd.take();
						--toTake;
						if (!postgresIndex.containsUser(mentioned.getMentionedUserID())) { 
							User mentionedUser = twitter.showUser(mentioned.getMentionedUserID());
							postgresIndex.updateUser(mentionedUser);
							postgresIndex.updateSubmits(mentioned.getUserID(), mentioned.getMentionedUserID(), mentioned.getTweetID());
						}  
					} else {
						status = twitter.getRateLimitStatus("users").get("/users/show/:id");
						if (isPrintingEnabled) {
							System.err.println("Going to sleep now for " + status.getSecondsUntilReset() + " seconds. queue size is: " + mentionedUsersToAdd.size());
						}
						Thread.sleep((status.getSecondsUntilReset() * 1000) + 10); 
						status = twitter.getRateLimitStatus("users").get("/users/show/:id");
						toTake = status.getRemaining();
						// if(isPrintingEnabled){
						// System.out.println("New limit to take: " + toTake);
						// }
					}
				}
			} catch (InterruptedException | TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	// private void resolve(long userID, Queue<Long> idsToResolve) throws
	// TwitterException, InterruptedException {
	// RateLimitStatus status =
	// twitter.getRateLimitStatus("users").get("/users/show/:id");
	// int idsToTake = status.getRemaining();
	//
	// Timestamp now = new Timestamp(System.currentTimeMillis());
	// while (!idsToResolve.isEmpty()) {
	// if (idsToTake > 0) {
	// long followerID = idsToResolve.remove();
	// User follower = twitter.showUser(followerID);
	// --idsToTake;
	// // if(isPrintingEnabled){
	// // System.out.println(userID + ", " + followerID);
	// // }
	//
	// if(isPrintingEnabled){
	// System.out.println("ID Queue size: " + idsToResolve.size() +
	// ", remaining id insertions: " + idsToTake + ", max id insertions: " +
	// status.getLimit());
	// }
	// postgresIndex.updateUser(follower);
	// postgresIndex.updateFollower(userID, follower.getId(), now);
	// } else {
	// status = twitter.getRateLimitStatus("users").get("/users/show/:id");
	// if(isPrintingEnabled){
	// System.err.println("Going to sleep now for " +
	// status.getSecondsUntilReset() + " seconds. id's to resolve are: " +
	// idsToResolve.size());
	// }
	// Thread.sleep((status.getSecondsUntilReset() * 1000) + 10);
	// status = twitter.getRateLimitStatus("users").get("/users/show/:id");
	// idsToTake = status.getRemaining();
	// // if(isPrintingEnabled){
	// // System.out.println("New limit of ids to take: " + idsToTake);
	// // }
	// }
	// }
	// }

	public void setPrintingEnabled(boolean isPrintingEnabled) {
		this.isPrintingEnabled = isPrintingEnabled;
	}

}
