package rest.uploadclient;

import index.textindex.utils.informationextractiontools.GermanTextInformationExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.bind.JAXBException;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;
import utils.dbcrud.DBDataManager;
import utils.dbcrud.DBTablesManager;
import utils.dbcrud.DocumentConsumer;
import utils.fileutils.FilePathRetriever;

public class UploadClient {
	public static void main(String[] args) throws JAXBException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";

		AbstractDBConnector db = new PGDBConnector(host, port, database, user, password);
		DBTablesManager dbManager = new DBTablesManager(db);

		BlockingQueue<String> documentQueue = new LinkedBlockingQueue<String>();
		DBDataManager dbDataManager = new DBDataManager(dbManager, new GermanTextInformationExtractor(), false); 
		dbDataManager.initializeDBTables();
		 

		documentQueue = new LinkedBlockingQueue<String>();
		DocumentConsumer consumer = new DocumentConsumer(dbDataManager, documentQueue);
		new Thread(consumer).start();

		String start = "C:/Users/rsp/Desktop/hikr_inputdata/hikrtextsfull";
		List<String> filePaths = new ArrayList<>();
		FilePathRetriever.INSTANCE.getFiles(new File(start), filePaths);
		int counter = 0;
		for (String file : filePaths) {
			counter++;
			if(counter == 10){
				break;
			}
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

		}

		while(!consumer.isUpdated()){ 
			try {
//				System.out.println("waiting..");
				Thread.sleep(1000);
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
			
		}
		
		consumer.setRunning(false);
		dbDataManager.close();
	}

}
