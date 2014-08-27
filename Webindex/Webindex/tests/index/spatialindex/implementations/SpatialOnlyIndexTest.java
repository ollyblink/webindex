package index.spatialindex.implementations;

import static org.junit.Assert.assertTrue;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.query.SpatialIndexQuery;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testutils.DBInitializer;
import utils.dbcrud.DBTablesManager;

public class SpatialOnlyIndexTest {
	private static SpatialOnlyIndex spatialOnlyIndex;
	private static DBTablesManager dbManager;

	@BeforeClass
	public static void init() {

		String[] docs = new String[] {
			"This text is about Zurich, a city in Switzerland",
			"The mayor of London was not amused",
			"Many people died in World War II when Berlin was bombed by the allies"
		};
		dbManager = DBInitializer.getTestDBManager();
		DBInitializer.initTestTextDB(new MockTextInformationExtractor(), dbManager, docs);
		spatialOnlyIndex = DBInitializer.initSpatialTestDB(dbManager, docs);
		
	}

	@Test
	public void queryTest() { 
//		Ranking ranking = spatialOnlyIndex.queryIndex(new SpatialIndexQuery("point_in", "Switzerland"));
//		 
//		for(Score d: ranking.getResults()){
// 			assertTrue(d.getId() == 1);
//		}
	}

	@AfterClass
	public static void tearDown() {
//		DBInitializer.tearDownTestDB(dbManager);
	}
}
