package rest.uploadclient;

import index.spatialindex.utils.geolocating.georeferencing.LocationFinder;
import index.textindex.utils.informationextractiontools.GermanTextInformationExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;
import utils.dbcrud.DocumentConsumer;
import utils.fileutils.FilePathRetriever;

public class UploadClient {
	private static final int NUMBER_TO_INDEX = 2000;

	public static void main(String[] args) throws Exception {
//		 String host = "geocomp-res.geo.uzh.ch";
//		 String port = "5432";
//		 String database = "girindex2";
//		 String user = "gcscript";
//		 String password = "gcmdp8057";

		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "postgres";
		Scanner scanner = new Scanner(System.in);

		AbstractDBConnector db = new PGDBConnector(host, port, database, user, password);
 
		DBTablesManager dbManager = new DBTablesManager(db);

		BlockingQueue<String> documentQueue = new LinkedBlockingQueue<String>();
		DBDataManager dbDataManager = new DBDataManager(dbManager, new GermanTextInformationExtractor(), false);
		dbDataManager.dropTables();
		dbDataManager.initializeDBTables();

		documentQueue = new LinkedBlockingQueue<String>();
		DocumentConsumer consumer = new DocumentConsumer(dbDataManager, documentQueue);
		new Thread(consumer).start();

		String start = "C:/Users/rsp/Dropbox/ArbeitenGeocomp/hikr_inputdata/hikrtextsfull";
		List<String> filePaths = new ArrayList<>();
		FilePathRetriever.INSTANCE.getFiles(new File(start), filePaths);
		int counter = 0;
		for (String file : filePaths) {
			counter++;
			// if(counter < 20){
			// continue;
			// }
			System.out.println("Indexing file " + counter);

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String text = "", line = null;
			while ((line = reader.readLine()) != null) {
				text += line + " ";
			}
			reader.close();

			try {
				documentQueue.put(text);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (counter == NUMBER_TO_INDEX) {
				while (!consumer.isUpdated()) {
					try {
						System.out.println("waiting for finishing..");
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// break;
				System.out.println("Do you want to index more? 1 means yes, any other integer means no.");
				try {
//					int answer = scanner.nextInt();
					int answer = 2;

					if (answer == 1) {
						System.out.println("Indexing continues with another " + NUMBER_TO_INDEX);
						counter = 0;
						continue;
					} else {
						System.out.println("Indexing aborted.");
						break;
					}
				} catch (Exception e) {
					System.out.println("Indexing aborted.");
					break;
				}

			}
		}

		while (!consumer.isUpdated()) {
			try {
				System.out.println("waiting for finishing..");
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		scanner.close();
		consumer.setRunning(false);
//		dbDataManager.close();
	}

}
