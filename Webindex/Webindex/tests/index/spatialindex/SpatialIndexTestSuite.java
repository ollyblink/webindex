package index.spatialindex;

import index.spatialindex.similarities.SpatialSimilarityTestSuite;
import index.spatialindex.utils.SpatialSimilaritiesUtilsTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SpatialOnlyIndexTest.class, SpatialSimilaritiesUtilsTestSuite.class, SpatialSimilarityTestSuite.class })
public class SpatialIndexTestSuite {

}