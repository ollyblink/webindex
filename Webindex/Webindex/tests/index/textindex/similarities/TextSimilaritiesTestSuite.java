package index.textindex.similarities;

import index.textindex.similarities.probabilisticmodels.BM1Test;
import index.textindex.similarities.vectorspacemodels.CosineSimilarityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BM1Test.class, CosineSimilarityTest.class})
public class TextSimilaritiesTestSuite {

}
