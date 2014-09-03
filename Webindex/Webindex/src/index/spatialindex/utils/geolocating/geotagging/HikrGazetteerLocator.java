package index.spatialindex.utils.geolocating.geotagging;

import index.spatialindex.utils.geolocating.georeferencing.HikrGazetteerPlaceExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import utils.dbconnection.AbstractDBConnector;

public class HikrGazetteerLocator {

	private ArrayList<String> hikrGazetteer;

	public HikrGazetteerLocator(AbstractDBConnector db, String dictionaryLocation, String tokenLocation) {
		hikrGazetteer = new ArrayList<String>();
		try {
			Statement statement = db.getConnection().createStatement();
			ResultSet set = statement.executeQuery("Select name from hikrgazetteer");
			while (set.next()) {
				hikrGazetteer.add(HikrGazetteerPlaceExtractor.alterText(set.getString(1)));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns possible toponyms for input text
	 * 
	 * @param inputText
	 * @return
	 */
	public HashSet<String> parse(String inputText) {
		ArrayList<String> possibleNames = new ArrayList<String>();

		String tokens[] = HikrGazetteerPlaceExtractor.alterText(inputText).split(" ");

		for (String token : tokens) {
			if (hikrGazetteer.contains(token)) {
				possibleNames.add(token);
			}
		}

		return new HashSet<>(possibleNames);
	}

	public static void main(String[] args) {
		System.out.println("Der Piz Medel ist ein grossfl�chiges Hochplateau / Eisplateau an der Grenze Tessin / Graub�nden. Das Massiv besteht aus dem Piz Cristallina 3128, Piz Uffiern 3151m, Cima di Camadra 3172m und dem H�chsten, der Piz Medel 3211m. Start in der Medelh�tte 2524m. Auf Wegspuren zum Glatscher da Plattas, unterhalb der� Fil Liung vorbei. �ber den flachen Glatscher da Plattas zum Skidepot unterhalb des Piz Medel. Am Schluss noch �ber einen kurzen und luftigen Grat auf den Gipfel des Piz Medel 3211m. Kurze Abfahrt und noch Gegenanstieg zum Piz Uffiern, der sich gut mit dem Piz Medel verbinden l�sst. Weiterer Bericht und Fotos zum Piz Medel auf: www.summitpost.org/Piz MedelAuch wenn der Piz Medel ein Romanischer Name hat, so ist er doch ein waschechter Tessiner 3000er (siehe dazu das interessante Buch von Ely Riva: F�nfzig 3000er des Tessin.".replaceAll("[^������a-zA-Z0-9\\s]", " "));
		// String host = "localhost";
		// String port = "5432";
		// String database = "girindex";
		// String user = "postgres";
		// String password = "32qjivkd";
		// String posModel = System.getProperty("user.dir")+"/src/index/spatialindex/utils/geolocating/geotagging/hikrmodels/de-pos-maxent.bin";
		// String tokens = System.getProperty("user.dir")+"/src/index/spatialindex/utils/geolocating/geotagging/hikrmodels/de-token.bin";
		//
		// HikrGazetteerLocator gazetter = new HikrGazetteerLocator(new PGDBConnector(host, port, database, user, password), posModel, tokens);
		// Set<String> locations = gazetter.parse("Das Matterhorn hab ich leider noch nie mein lieber �sseren Herr Gesangsverein Sport is Mord gesehen.");
		// for (String loc : locations) {
		// System.out.println(loc);
		// }
	}
}
