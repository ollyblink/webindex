package index.spatialindex.utils;

import static org.junit.Assert.*;
import index.textindex.implementations.DBIndexTest;
import index.textindex.utils.texttransformation.GermanTextTokenizer;
import index.utils.DBDataProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBDataProviderTest {

	public static final int QUEUE_SIZE = 100;

	@Before
	public void setUp() throws Exception {
		DBDataProvider provider = new DBDataProvider(DBIndexTest.dbManager, new GermanTextTokenizer(), QUEUE_SIZE);
		 
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
