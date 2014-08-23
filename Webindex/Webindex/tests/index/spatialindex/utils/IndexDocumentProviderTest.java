package index.spatialindex.utils;

import static org.junit.Assert.*;
import index.textindex.DBIndexTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IndexDocumentProviderTest {

	@Before
	public void setUp() throws Exception {
		IndexDocumentProvider provider = new IndexDocumentProvider(DBIndexTest.dbManager);
		 
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
