package twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * 
 * @author MSc. Oliver F. M. Zihler
 * 
 *         This class abstracts the parsing of the properties file for the keys
 *         needed to connect to twitter. <code>TwitterBuilder</code> returns a
 *         new <code>Twitter</code> object to perform queries on Twitter
 *
 */
public enum TwitterBuilder {

	INSTANCE;
	private static final String twitterCredentialsPropertyFileLocation = System.getProperty("user.dir") + "/src/main/resources/twitter.properties";

	/**
	 * Not to be instantiated
	 * 
	 */
	private TwitterBuilder() {

	}

	/**
	 * Lot's of stuff you don't need to worry about. Basically loading the
	 * credentials and access keys for the twitter api.
	 * 
	 * @param twitterCredentialsPropertyFileLocation
	 *            where the credentials are stored
	 * @return
	 */
	public Twitter newInstance() {
		try {
			Properties credentialProperties = new Properties();

			credentialProperties.load(new FileInputStream(twitterCredentialsPropertyFileLocation));

			ConfigurationBuilder configBuilder = new ConfigurationBuilder().setDebugEnabled(true).setOAuthConsumerKey(credentialProperties.getProperty("consumerkey"))
					.setOAuthConsumerSecret(credentialProperties.getProperty("consumersecret")).setOAuthAccessToken(credentialProperties.getProperty("accesstoken"))
					.setOAuthAccessTokenSecret(credentialProperties.getProperty("accesstokensecret"));

			return new TwitterFactory(configBuilder.build()).getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ConfigurationBuilder newConfigurationBuilder() {
		try {
			Properties credentialProperties = new Properties();

			credentialProperties.load(new FileInputStream(twitterCredentialsPropertyFileLocation));

			ConfigurationBuilder configBuilder = new ConfigurationBuilder().setDebugEnabled(true).setOAuthConsumerKey(credentialProperties.getProperty("consumerkey"))
					.setOAuthConsumerSecret(credentialProperties.getProperty("consumersecret")).setOAuthAccessToken(credentialProperties.getProperty("accesstoken"))
					.setOAuthAccessTokenSecret(credentialProperties.getProperty("accesstokensecret"));

			return configBuilder;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}