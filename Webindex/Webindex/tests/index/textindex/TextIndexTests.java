package index.textindex;

import index.textindex.implementations.RAMTextOnlyIndexTest;
import index.textindex.similarities.TextSimilaritiesTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({RAMTextOnlyIndexTest.class,TextSimilaritiesTestSuite.class})
public class TextIndexTests {

}
