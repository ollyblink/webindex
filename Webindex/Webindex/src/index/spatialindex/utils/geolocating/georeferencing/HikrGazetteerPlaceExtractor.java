package index.spatialindex.utils.geolocating.georeferencing;

import index.spatialindex.utils.GeometryConverter;

import java.rmi.NoSuchObjectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

	private AbstractDBConnector db;

	public HikrGazetteerPlaceExtractor(AbstractDBConnector db) {
		this.db = db;
	}

	@Override
	public ArrayList<Geometry> extract(String text) {
		String sql = "SELECT GEOMETRY FROM HIKRGAZETTEER WHERE lower(NAME) LIKE lower('%" + text.replace(" ", "%") + "%')";

		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		try {
			Statement statement = db.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);
			while(set.next()){ 
				Geometry footprint = GeometryConverter.convertPostGisToJTS((PGgeometry) set.getObject(1));
				geometries.add(footprint);
			}
			statement.close();
		} catch (SQLException | NoSuchObjectException e) {
			e.printStackTrace();
		}
		return geometries;
	}

	public static void main(String[] args) {

		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";
		HikrGazetteerPlaceExtractor gazetter = new HikrGazetteerPlaceExtractor(new PGDBConnector(host, port, database, user, password));
		ArrayList<Geometry> geoms = gazetter.extract("sseren");
		for(Geometry geom:geoms){
			System.out.println(geom);
		}
	}
}
