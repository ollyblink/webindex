package index.girindex.combinationstrategy;

import index.girindex.combinationstrategy.bordacounts.SimpleBordaCountTest;
import index.girindex.combinationstrategy.combfamily.CombMNZTest;
import index.girindex.combinationstrategy.utils.NormalizerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({NormalizerTest.class, CombMNZTest.class, SimpleBordaCountTest.class})
public class CombinationStrategyTestSuite {

}
