package index.spatialindex.utils.geolocating.georeferencing;

import index.spatialindex.utils.GeometryConverter;

import java.rmi.NoSuchObjectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
//	String host = "localhost";
//	String port = "5432";
//	String database = "girindex2";
//	String user = "postgres";
//	String password = "postgres";
	private HashMap<String, Geometry> hikrGazetteer;

	public HikrGazetteerPlaceExtractor(String host, String port, String database, String user, String password) {
		AbstractDBConnector db = new PGDBConnector(host, port, database, user, password);
		hikrGazetteer = new HashMap<String, Geometry>();
		try {
			Statement statement = db.getConnection().createStatement();
			ResultSet set = statement.executeQuery("Select name, geometry from hikrgazetteer");
			while (set.next()) {
				hikrGazetteer.put(alterText(set.getString(1)), GeometryConverter.convertPostGisToJTS((PGgeometry) set.getObject(2)));
			}
			statement.close();
		} catch (SQLException | NoSuchObjectException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Geometry> extract(String text) {
		String[] tokens = alterText(text).split(" ");
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		for (String token : tokens) {
			if (hikrGazetteer.containsKey(token)) {
				geometries.add(hikrGazetteer.get(token));
			}
		}
		return geometries;
	}

	public static String alterText(String text) {
		return text.replaceAll("[^öÖäÄüÜa-zA-Z0-9\\s]", " ");
	}

	public static void main(String[] args) {
//		String host = "geocomp-res.geo.uzh.ch"; 
//		HikrGazetteerPlaceExtractor gazetter = new HikrGazetteerPlaceExtractor();
//		ArrayList<Geometry> geoms = gazetter.extract("sseren");
//		for (Geometry geom : geoms) {
//			System.out.println(geom);
//		}
	}
}
