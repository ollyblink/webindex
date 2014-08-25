package index.spatialindex.implementations;

import static org.junit.Assert.*;

import java.util.ArrayList;

import index.spatialindex.implementations.SpatialOnlyIndex;
import index.textindex.utils.texttransformation.MockTextTokenizer;
import index.utils.DBManager;
import index.utils.IndexDocument;
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
			"The mayor of London was not amused",
			"Many people died in World War II when Berlin was bombed by the allies"
		};
		dbManager = DBManager.getTestDBManager();
		DBManager.initTestTextDB(new MockTextTokenizer(), dbManager, docs);
		spatialOnlyIndex = DBManager.initSpatialTestDB(dbManager, docs);
		
	}

	@Test
	public void queryTest() { 
		Ranking ranking = spatialOnlyIndex.queryIndex("point_in", "Switzerland");
		ArrayList<IndexDocument> results = ranking.getResults();
		for(IndexDocument d: results){
 			assertTrue(d.getId() == 1);
		}
	}

	@AfterClass
	public static void tearDown() {
		DBManager.tearDownTestDB(dbManager);
	}
}
