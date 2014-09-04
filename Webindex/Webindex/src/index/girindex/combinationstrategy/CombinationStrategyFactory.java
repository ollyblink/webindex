package index.girindex.combinationstrategy;

import index.girindex.combinationstrategy.bordacounts.BordaCount;
import index.girindex.combinationstrategy.combfamily.CombAnz;
import index.girindex.combinationstrategy.combfamily.CombMNZ;
import index.girindex.combinationstrategy.combfamily.CombMax;
import index.girindex.combinationstrategy.combfamily.CombMin;
import index.girindex.combinationstrategy.combfamily.CombSum;

public class CombinationStrategyFactory {

	public static ICombinationStrategy create(String combinationstrategy) {
		switch (combinationstrategy) { 
		case "simpleborda":
			return new BordaCount();
		case "combmin":
			return new CombMin();
		case "combmax":
			return new CombMax();
		case "combsum":
			return new CombSum();
		case "combanz":
			return new CombAnz();
		case "combmnz":
		default:
			return new CombMNZ();
		}
	}
}
