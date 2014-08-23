package index.spatialindex;

import index.spatialindex.spatialindeximplementations.SpatialOnlyIndex;
import index.utils.DBManager;
import index.utils.Ranking;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SpatialOnlyIndexTest {
	private static SpatialOnlyIndex spatialOnlyIndex;
	private static DBManager dbManager;

	@BeforeClass
	public static void init() {

		String[] docs = new String[] {
			"This text is about Zurich, a city in Switzerland",
			"The queen of England was not amused",
			"Many people died in World War II when Berlin was bombed by the allies"
		};
		dbManager = DBManager.getTestDBManager();
		spatialOnlyIndex = DBManager.initSpatialTestDB(dbManager, docs);
		
	}

	@Test
	public void queryTest() { 
		Ranking ranking = spatialOnlyIndex.queryIndex("point_in", "Switzerland");
		ranking.getResults();
	}

	@AfterClass
	public static void tearDown() {
		DBManager.tearDownTestDB(dbManager);
	}
}
