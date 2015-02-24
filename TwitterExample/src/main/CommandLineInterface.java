package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import twitter.TwitterBuilder;
import twitter4j.Twitter;
import db.AbstractDBConnector;
import db.PGDBConnector;
import db.TwitterStreamDBCreatorAndWriter;

public class CommandLineInterface {
	private static final String twitterCredentialsPropertyFileLocation = System.getProperty("user.dir") + "/src/main/resources/twitter.properties";

	public static void main(String[] args) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);

		// Database things
		System.err.println("Please enter host,port,database,user,password in that format (separated by comma):");
		String[] databaseInfo = scanner.nextLine().split(",");
		String host = databaseInfo[0];
		String port = databaseInfo[1];
		String database = databaseInfo[2];
		String user = databaseInfo[3];
		String password = databaseInfo[4];

		System.err.println("Please enter the keywords to filter for, separated by comma (e.g. kw1,kw2,...,k2n):");
		System.err.println("Type in nothing and press enter if you want to read a csv-file instead");
		String[] keywordsToFilterFor = scanner.nextLine().split(",");
		if (keywordsToFilterFor.length == 1 && keywordsToFilterFor[0].trim().length() == 0) {
			keywordsToFilterFor = null;
		}

		// READ FROM FILE...
		if (keywordsToFilterFor == null || keywordsToFilterFor.length == 0) {
			File fileToRead = null;
			while (fileToRead == null) {
				System.err.println("Please enter the full path to a csv file on your computer to extract keywords from to filter");
				System.err.println("Each word should be separated by commas (,).");
				fileToRead = new File(scanner.nextLine());
				if (!fileToRead.exists() || !fileToRead.isFile()) {
					fileToRead = null;
					System.err.println("The file seems to either not exist or not be a file.");
					System.err.println("Please try again.");
				} else {
					keywordsToFilterFor = getKeywordsFromFile(fileToRead);
					if (keywordsToFilterFor == null || keywordsToFilterFor.length == 0) {
						fileToRead = null;
						System.err.println("The file you provided seems to be empty.");
						System.err.println("Please try again.");
					}
				}
			}
		}

		// After reading from file...

		keywordsToFilterFor = removeEmptyStrings(keywordsToFilterFor);
		System.err.print("Keywords to filter tweets for: ");
		for (String keyword : keywordsToFilterFor) {
			System.err.print(keyword + ", ");
		}
		System.out.println();

		Thread.sleep(1000);

		System.err.println("Do you only want to save tweets that have a geolocation? (0 = no/1 = yes)");
		boolean mustHaveGeolocation = (scanner.nextInt() == 1 ? true : false);

		System.err.println("Do you want to show all incoming tweets on the command line? (0 = no/ 1 = yes)");
		boolean canShowTweets = (scanner.nextInt() == 1 ? true : false);
		
		System.err.println("Do you want to filter only specific languages? enter languages like en,de, ...");
		 
		String[] languagesStrings = scanner.nextLine().split(",");
		List<String> languagesList = new ArrayList<String>();
		for(String language: languagesStrings) {
			if(language.trim().length() > 0){
				languagesList.add(language.trim());
			}
		}
		String[] languages = new String[languagesList.size()];
		for(int i = 0; i < languages.length;++i){
			languages[i] = languagesList.get(i);
		}
		System.err.println("Tweets will now be processed.");

		// INITIALISE SYSTEM
		AbstractDBConnector postgresConnector = new PGDBConnector(host, port, database, user, password);
		TwitterStreamDBCreatorAndWriter postgresIndex = new TwitterStreamDBCreatorAndWriter(postgresConnector, false);

		Twitter twitter = null;
		try {
			twitter = TwitterBuilder.INSTANCE.newInstance(twitterCredentialsPropertyFileLocation);
		} catch (NoSuchFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BlockingQueue<UserMentionTuple> mentionedUsersToAdd = null;

		// Save followers if needed...
		mentionedUsersToAdd = new LinkedBlockingQueue<UserMentionTuple>();
		MentionedUserAdder consumer = new MentionedUserAdder(mentionedUsersToAdd, postgresIndex, twitter);
		consumer.setPrintingEnabled(true);
		consumer.start();

		// Normal processing if no followers are being processed
		TwitterStreamingThread producer = new TwitterStreamingThread(keywordsToFilterFor, languages, mentionedUsersToAdd, postgresIndex, canShowTweets, mustHaveGeolocation);
		producer.start();

		// STOP STREAMING
		System.err.println("To stop streaming, please enter the word stop");

		if (scanner.next("stop").equals("stop")) {
			System.err.println("Stopping streaming");
			producer.exit(true);

			System.err.println("Please wait until all the followers have been saved");
			System.err.println("Due to limitations of 150 user look ups per 15 minutes, this may take a while");
			System.err.println("Depending on the size of followers to add, this may take minutes, hours, or even days, so don't turn the system off.");
			System.err.println("The system will shut down on its own once all followers have been saved to the database.");
			while (!mentionedUsersToAdd.isEmpty()) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			consumer.exit(true);
			System.err.println("Finished saving all followers!");
		}
		System.err.println("System will now shut down.");
		System.exit(0);

		scanner.close();
	}

	private static String[] removeEmptyStrings(String[] keywordsToFilterFor) {
		List<String> tmp = new LinkedList<String>();
		for (String k : keywordsToFilterFor) {
			if (k.trim().length() > 0) {
				tmp.add(k);
			}
		}
		return convertListToArray(tmp);
	}

	private static String[] convertListToArray(List<String> tmp) {
		String[] keywordList = new String[tmp.size()];
		for (int i = 0; i < tmp.size(); ++i) {
			keywordList[i] = tmp.get(i);
		}
		return keywordList;
	}

	private static String[] getKeywordsFromFile(File fileToRead) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
			List<String> keywords = new ArrayList<String>();
			String line = null;
			while ((line = reader.readLine()) != null) {
				for (String keyword : line.split(",")) {
					keywords.add(keyword);
				}
			}
			reader.close();
			return convertListToArray(keywords);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
