package testsuite;

import index.combinationstrategy.utils.NormalizerTest;
import index.spatialindex.similarities.SpatialRelationshipFactoryTest;
import index.spatialindex.utils.LocationProviderTest;
import index.textindex.DBIndexTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import similarities.spatialsimilarities.pointsimilarities.InRelationshipTest;
import similarities.textsimilarities.probabilisticmodels.BM1Test;
import similarities.textsimilarities.vectorspacemodels.CosineSimilarityTest;

@RunWith(Suite.class)
@SuiteClasses({ DBIndexTest.class, CosineSimilarityTest.class, BM1Test.class, LocationProviderTest.class, SpatialRelationshipFactoryTest.class, InRelationshipTest.class, NormalizerTest.class})
public class AllTests {

}
