package initGazeteer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.postgis.Geometry;
import org.postgis.Point;

import utils.dbconnection.AbstractDBConnector;
import utils.dbconnection.PGDBConnector;

public class InitializeGazetzeer {
	public static final String gazetteer = "create table hikrgazetteer(id bigserial primary key, objectval varchar(100), name varchar(100)); Select ADDGEOMETRYCOLUMN('hikrgazetteer','geometry',4326,'GEOMETRY',2); Create Index hikrgazetteer3_spatial_index on locations using GIST(geometry);";
	private static AbstractDBConnector db;

	public static void main(String[] args) throws IOException, SQLException {
		createGazeteer();

		String gazeteerCsv = "tests/tryouts/HIKRgazetteer.csv";

		Statement statement = db.getConnection().createStatement();

		CSVParser parser = CSVParser.parse(new File(gazeteerCsv), StandardCharsets.ISO_8859_1, CSVFormat.EXCEL);
		List<CSVRecord> records = parser.getRecords();

		int counter = 0;
		for (CSVRecord record : records) { 
			if(counter++ == 0){
				continue; //Title
			}
			Geometry geom = new Point(Double.parseDouble(record.get(3)), Double.parseDouble(record.get(4)));
			geom.setSrid(21781);
			String geomSQL = "ST_Transform(ST_GEOMFROMEWKT('" + geom + "'),4326" + ")";
			System.out.println("insert into hikrgazetteer values (" + record.get(0) + ",'" + record.get(1).replace("'", "''") + "','" + record.get(2).replace("'", "''") + "'," + geomSQL + ");");
			statement.executeUpdate("insert into hikrgazetteer values (" + record.get(0) + ",'" + record.get(1).replace("'", "''") + "','" + record.get(2).replace("'", "''") + "'," + geomSQL + ");");
		}

		statement.close();
	}

	private static void createGazeteer() throws SQLException {
		String host = "localhost";
		String port = "5432";
		String database = "girindex";
		String user = "postgres";
		String password = "32qjivkd";
		db = new PGDBConnector(host, port, database, user, password);
		if (!db.tableExists("hikrgazetteer")) {
			Statement statement = db.getConnection().createStatement();
			statement.execute(gazetteer);
			statement.close();

		}
	}
}
