package index.spatialindex.similarities;

import index.spatialindex.similarities.pointsimilarities.PointSimilarityTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SpatialRelationshipFactoryTest.class, PointSimilarityTestSuite.class })
public class SpatialSimilarityTestSuite {

}
