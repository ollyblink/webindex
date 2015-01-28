package main;

public final class UserMentionTuple {
	private final long userID;
	private final long mentionedUserID;
	private final long tweetID;

	public UserMentionTuple(final long userID, final long mentionedUserID, final long tweetID) {
		this.userID = userID;
		this.mentionedUserID = mentionedUserID;
		this.tweetID = tweetID;
	}

	public long getUserID() {
		return userID;
	}

	public long getMentionedUserID() {
		return mentionedUserID;
	}

	public long getTweetID() {
		return tweetID;
	}

}
