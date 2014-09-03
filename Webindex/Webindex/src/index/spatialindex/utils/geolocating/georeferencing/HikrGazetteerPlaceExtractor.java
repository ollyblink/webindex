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

	private HashMap<String, Geometry> hikrGazetteer;

	public HikrGazetteerPlaceExtractor(AbstractDBConnector db) {
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
		for(String token:tokens){
			if(hikrGazetteer.containsKey(token)){
				geometries.add(hikrGazetteer.get(token));
			}
		}
		return geometries;
	}

	public static String alterText(String text) {
		return text.replaceAll("[^öÖäÄüÜa-zA-Z0-9\\s]", " ");
	}

	public static void main(String[] args) {

		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";
		HikrGazetteerPlaceExtractor gazetter = new HikrGazetteerPlaceExtractor(new PGDBConnector(host, port, database, user, password));
		ArrayList<Geometry> geoms = gazetter.extract("sseren");
		for (Geometry geom : geoms) {
			System.out.println(geom);
		}
	}
}
