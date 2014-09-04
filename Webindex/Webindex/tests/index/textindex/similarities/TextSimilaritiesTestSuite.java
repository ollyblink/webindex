package index.textindex.similarities;

import index.textindex.similarities.booleanmodels.SimpleBooleanSimilarity;
import index.textindex.similarities.probabilisticmodels.BM11Test;
import index.textindex.similarities.probabilisticmodels.BM15Test;
import index.textindex.similarities.probabilisticmodels.BM1Test;
import index.textindex.similarities.probabilisticmodels.BM25Test;
import index.textindex.similarities.vectorspacemodels.CosineSimilarityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BM1Test.class, BM11Test.class,BM15Test.class,BM25Test.class,CosineSimilarityTest.class, SimpleBooleanSimilarity.class})
public class TextSimilaritiesTestSuite {

}
