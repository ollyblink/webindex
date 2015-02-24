package index.spatialindex.utils.geolocating.georeferencing;

import index.spatialindex.utils.GeometryConverter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.rmi.NoSuchObjectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.postgis.PGgeometry;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Queries the hikr gazetteer database table to extract locations
 * 
 * @author rsp
 *
 */
public class HikrGazetteerPlaceExtractor implements IPlaceExtractor {
	private HashMap<String, Geometry> hikrGazetteer;

	public HikrGazetteerPlaceExtractor(AbstractDBConnector db) {
		hikrGazetteer = new HashMap<String, Geometry>();
		try {
			Statement statement = db.getConnection().createStatement();
			ResultSet set = statement.executeQuery("Select name, geometry from hikrgazetteer");
			while (set.next()) {
				hikrGazetteer.put(/* alterText( */set.getString(1)/* ) */, GeometryConverter.convertPostGisToJTS((PGgeometry) set.getObject(2)));
			}
			statement.close();
		} catch (SQLException | NoSuchObjectException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Geometry> extract(String text) {
		text = alterText(text);
		Set<String> smallIndex = createSmallIndex(text);

		Map<String, Geometry> temp = new HashMap<String, Geometry>();

		Map<String, Integer> counter = new HashMap<String, Integer>();

		for (String geoname : hikrGazetteer.keySet()) {
			if (text.contains(geoname)) {
				addToCounter(counter, geoname);
			}
			// if(smallIndex.contains(geoname)){
			// addToCounter(counter, geoname);
			// }
		}

		for (String s : counter.keySet()) {
			System.out.println(s + " " + counter.get(s));
		}

		// System.out.println(geoname);
		// temp.put(geoname, hikrGazetteer.get(geoname));
		// temp.put(geoname, hikrGazetteer.get(geoname));
		// HashMap<String, Boolean> containment = new HashMap<String, Boolean>();
		// for (String key1 : temp.keySet()) {
		// boolean isContainedIn = false;
		// for (String key2 : temp.keySet()) {
		// if (key1.equals(key2)) {
		// continue;
		// } else {
		// if (key2.contains(key1)) {
		// isContainedIn = true;
		// break;
		// }
		// }
		// }
		// if (isContainedIn) {
		// containment.put(key1, true);
		// } else {
		// System.out.println(key1);
		// geometries.add(hikrGazetteer.get(key1));
		// }
		// }

		return new ArrayList<Geometry>(temp.values());
	}

	private void addToCounter(Map<String, Integer> counter, String geoname) {
		Integer cnt = counter.get(geoname);
		if (cnt == null) {
			cnt = 0;
		}
		++cnt;
		counter.put(geoname, cnt);
	}

	public static String alterText(String text) {
		return text.replaceAll("[^öÖäÄüÜa-zA-Z0-9\\s]", " ");
	}

	private Set<String> createSmallIndex(String text) {
		String tmp = alterText(text);
		Set<String> smallIndex = new HashSet<String>();
		String[] split = tmp.split("\\s");
		for (String s : split) {
			if (s.trim().length() > 0) {
				 System.out.println(s);
				smallIndex.add(s);
			}
		}
		return smallIndex;
	}

	public static void main(String[] args) {
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "postgres";
		AbstractDBConnector con = new PGDBConnector(host, port, database, user, password);
		HikrGazetteerPlaceExtractor gazetter = new HikrGazetteerPlaceExtractor(con);
		ArrayList<IPlaceExtractor> extractors = new ArrayList<IPlaceExtractor>();
		extractors.add(gazetter);
		LocationFinder finder = new LocationFinder(extractors);

		String text = getText("C:/Users/rsp/Dropbox/ArbeitenGeocomp/hikr_inputdata/hikrtextsfull/57257.txt");

		System.out.println(text);
		ArrayList<Geometry> geoms = finder.findLocation(text);
		for (Geometry geom : geoms) {
			System.out.println(geom);
		}

	}

	private static String getText(String file) {
		String text = "";

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				text += line + " ";
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}
