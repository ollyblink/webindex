package mapdisplay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgis.PGgeometry;

import db.AbstractDBConnector;
import db.PGDBConnector;

public class TweetsMapping {

	private static final String host = "localhost";
	private static final String port = "5432";
	private static final String database = "postgres";
	private static final String user = "postgres";
	private static final String password = "32qjivkd";
	private static final String sqlQuery = "Select tweet_text, geolocation from tweets;";

	private static AbstractDBConnector db;

	public static void main(String[] args) throws IOException, SQLException, InterruptedException {
		db = new PGDBConnector(host, port, database, user, password);
		System.out.println("Select tweet_text, geolocation from tweets;");
		 
		
		String html = "<!doctype html><html ng-app=\"maphandler\"><head><title>Map</title><style type=\"text/css\">#leafletcontainer {width: 1024px;height: 800px;}</style><script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.2.22/angular.js\"></script>  <script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.2.22/angular-resource.js\"></script> <script src=\"http://tombatossals.github.io/angular-leaflet-directive/bower_components/angular/angular.min.js\"></script><script src=\"http://tombatossals.github.io/angular-leaflet-directive/bower_components/leaflet/dist/leaflet.js\"></script> <script src=\"http://tombatossals.github.io/angular-leaflet-directive/dist/angular-leaflet-directive.min.js\"></script> <link rel=\"stylesheet\" href=\"http://tombatossals.github.io/angular-leaflet-directive/bower_components/leaflet/dist/leaflet.css\" /><script type=\"text/javascript\">"
				+ " angular.module('maphandler', ['leaflet-directive']).controller('MapCtrl', function ($scope) {angular.extend($scope, {switzerlandcentre: { lat: 47.5, lng: 8.5, zoom: 6 }, paths: {} });  $scope.markers = new Array();" + getLocations() + "});</script></head><body><div id=\"mapdiv\"  ng-controller=\"MapCtrl\"><leaflet id=\"leafletcontainer\" center=\"switzerlandcentre\" markers=\"markers\"></leaflet></div></body></html>";

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("map.html")));
		writer.write(html);
		writer.close();
	}

	private static String getLocations() throws SQLException {
		Statement s = db.getConnection().createStatement();
		String resultLocations = "";
		ResultSet set = s.executeQuery(sqlQuery);
		System.out.println(sqlQuery);
		while (set.next()) {
			
			String tweet = set.getString(1);
			PGgeometry geom = (PGgeometry) set.getObject(2); 
			resultLocations += "$scope.markers.push({" + "  lat: " + geom.getGeometry().getLastPoint().y + ", lng: " + geom.getGeometry().getLastPoint().x + ", message: \"" + "" + "\" });";
		}
		System.out.println(resultLocations);

		return resultLocations;
	}
}
