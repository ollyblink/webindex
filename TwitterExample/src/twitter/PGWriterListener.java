package twitter;

import java.util.concurrent.BlockingQueue;

import main.UserMentionTuple;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.UserMentionEntity;
import db.TwitterStreamDBCreatorAndWriter;

public class PGWriterListener implements StatusListener {

	private final TwitterStreamDBCreatorAndWriter indexCreator;
	private final String[] queries;
	private boolean isDisplayTweetsEnabled;
	private boolean mustHaveGeolocation;
	private BlockingQueue<UserMentionTuple> mentionedUsers;

	public PGWriterListener(BlockingQueue<UserMentionTuple> mentionedUsers, TwitterStreamDBCreatorAndWriter indexCreator, String[] queries, boolean isDisplayTweetsEnabled, boolean mustHaveGeolocation) {
		this.mentionedUsers = mentionedUsers;
		this.indexCreator = indexCreator;
		this.queries = queries;
		this.isDisplayTweetsEnabled = isDisplayTweetsEnabled;
		this.mustHaveGeolocation = mustHaveGeolocation;
		// Always call insertQueries before calling insertStreamTweet, because
		// else, the queries are not yet present in the database
		indexCreator.insertQueries(queries);
	}

	@Override
	public void onStatus(Status status) {
		if (checkIfGeoLocationIsEnabled(status.getGeoLocation())) {
			if (isDisplayTweetsEnabled) {
				System.out.println("@" + status.getUser().getScreenName() + " location: " + status.getGeoLocation() + " - " + status.getText());
			} 
			try {
				indexCreator.insertStreamTweet(status, queries);

				if (mentionedUsers != null) {
					UserMentionEntity[] mentioned = status.getUserMentionEntities();
					for (UserMentionEntity user : mentioned) {
						mentionedUsers.put(new UserMentionTuple(status.getUser().getId(), user.getId(), status.getId()));
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkIfGeoLocationIsEnabled(GeoLocation geoLocation) {
		if (mustHaveGeolocation) {
			return geoLocation != null;
		} else {
			return true;
		}
	}

	public void setMustHaveGeoLocation(boolean hasGeoLocation) {
		mustHaveGeolocation = hasGeoLocation;
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		if (isDisplayTweetsEnabled) {
			System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
		}
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		if (isDisplayTweetsEnabled) {
			System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
		}
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		if (isDisplayTweetsEnabled) {
			System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
		}
	}

	@Override
	public void onException(Exception ex) {
		if (isDisplayTweetsEnabled) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onStallWarning(StallWarning sW) {
		if (isDisplayTweetsEnabled) {
			System.out.println(sW.getMessage());
		}
	}

	public void showReceivedTweets(boolean isDisplayTweetsEnabled) {
		this.isDisplayTweetsEnabled = isDisplayTweetsEnabled;
	}

};