package testsuite;

import index.girindex.GIRIndexTestSuite;
import index.spatialindex.SpatialIndexTestSuite;
import index.textindex.TextIndexTests;
import index.utils.UtilsTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UtilsTestSuite.class, TextIndexTests.class, SpatialIndexTestSuite.class, GIRIndexTestSuite.class})
public class AllTests {

}
