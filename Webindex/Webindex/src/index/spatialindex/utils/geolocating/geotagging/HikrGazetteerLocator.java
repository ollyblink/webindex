package index.spatialindex.utils.geolocating.geotagging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;

public class HikrGazetteerLocator {
	private AbstractDBConnector db;
	private POSTagger tagger;
	private Tokenizer tokenizer;

	public HikrGazetteerLocator(AbstractDBConnector db, String dictionaryLocation, String tokenLocation) {
		this.db = db;
		this.tagger = initializeDefaultTagger(dictionaryLocation);
		this.tokenizer = initializeDefaultTokenizer(tokenLocation);
	}

	/**
	 * Initialises the dictionary for noun extraction
	 * 
	 * @param dictionaryLocation
	 *            where the dictonary is stored
	 */
	private POSTagger initializeDefaultTagger(String dictionaryLocation) {
		POSTagger tagger = null;
		try {
			InputStream modelIn;
			modelIn = new FileInputStream(dictionaryLocation);
			POSModel model = new POSModel(modelIn);
			tagger = new POSTaggerME(model);
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		}
		return tagger;
	}

	/**
	 * Initialises the list of tokens to use
	 * 
	 * @param tokenLocation
	 *            where the tokens are stored.
	 */
	private Tokenizer initializeDefaultTokenizer(String tokenLocation) {
		Tokenizer tokenizer = null;
		try {
			InputStream modelIn = new FileInputStream(tokenLocation);
			TokenizerModel model = new TokenizerModel(modelIn);
			tokenizer = new TokenizerME(model);
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		}
		return tokenizer;
	}

	/**
	 * Returns possible toponyms for input text
	 * 
	 * @param inputText
	 * @return
	 */
	public HashSet<String> parse(String inputText) {
		ArrayList<String> possibleNames = new ArrayList<String>();

		String tokens[] = tokenize(inputText);
		String tags[] = tagger.tag(tokens);
		for (int i = 0; i < tags.length; ++i) {
			if (tags[i].equals("NN") || tags[i].equals("NNS") || tags[i].equals("NNP") || tags[i].equals("NNPS")) {
				possibleNames.add(tokens[i]);
			}
		}

		String whereClause = " where ";
		int counter = 0;
		for (String token : possibleNames) {
			token = token.replaceAll("[^a-zA-Z0-9\\s]", "%");
			if (++counter == possibleNames.size()) {
				whereClause += "lower(name) like lower('" + token + "')";
			} else {
				whereClause += "lower(name) like lower('" + token + "') OR ";
			}
		}

		whereClause += ";";
		possibleNames.clear();

		String sql = "Select name from hikrgazetteer " + whereClause;

		System.out.println(sql);
		try {
			Statement statement = db.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				possibleNames.add(set.getString(1));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new HashSet<>(possibleNames);
	}

	/**
	 * tokenizes a sentence
	 * 
	 * @param sentence
	 *            to tokenize
	 */
	protected String[] tokenize(String sentence) {
		String toTokenize = (sentence == null) ? "" : sentence;

		// Get rid of special characters
		toTokenize = toTokenize.replaceAll("\\n", " ");
		toTokenize = toTokenize.substring(0, toTokenize.length() - 1);
		toTokenize = toTokenize.replaceFirst("\\.", "");
		toTokenize = toTokenize.replaceAll("\\,", "");
		toTokenize = toTokenize.replaceAll("\\[", "");
		toTokenize = toTokenize.replaceAll("]", "");

		return tokenizer.tokenize(toTokenize);
	}

	public static void main(String[] args) {

		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";
		String posModel = System.getProperty("user.dir")+"/src/index/spatialindex/utils/geolocating/geotagging/hikrmodels/de-pos-maxent.bin";
		String tokens = System.getProperty("user.dir")+"/src/index/spatialindex/utils/geolocating/geotagging/hikrmodels/de-token.bin";
		
		HikrGazetteerLocator gazetter = new HikrGazetteerLocator(new PGDBConnector(host, port, database, user, password), posModel, tokens);
		Set<String> locations = gazetter.parse("Das Matterhorn hab ich leider noch nie mein lieber Üsseren Herr Gesangsverein Sport is Mord gesehen.");
		for (String loc : locations) {
			System.out.println(loc);
		}
	}
}
