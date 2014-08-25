package testsuite;

import index.girindex.combinationstrategy.utils.NormalizerTest;
import index.spatialindex.similarities.SpatialRelationshipFactoryTest;
import index.spatialindex.similarities.pointsimilarities.InRelationshipTest;
import index.spatialindex.utils.LocationProviderTest;
import index.textindex.implementations.DBIndexTest;
import index.textindex.similarities.probabilisticmodels.BM1Test;
import index.textindex.similarities.vectorspacemodels.CosineSimilarityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DBIndexTest.class, CosineSimilarityTest.class, BM1Test.class, LocationProviderTest.class, SpatialRelationshipFactoryTest.class, InRelationshipTest.class, NormalizerTest.class})
public class AllTests {

}
