package testsuite;

import index.girindex.combinationstrategy.utils.NormalizerTest;
import index.spatialindex.similarities.SpatialRelationshipFactoryTest;
import index.spatialindex.similarities.pointsimilarities.InRelationshipTest;
import index.spatialindex.utils.LocationProviderTest;
import index.textindex.similarities.TextSimilaritiesTestSuite;
import index.utils.DBDataProviderTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DBDataProviderTest.class,TextSimilaritiesTestSuite.class, LocationProviderTest.class, SpatialRelationshipFactoryTest.class, InRelationshipTest.class, NormalizerTest.class})
public class AllTests {

}
