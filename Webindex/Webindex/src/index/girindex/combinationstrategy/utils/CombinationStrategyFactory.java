package index.girindex.combinationstrategy.utils;

import index.girindex.combinationstrategy.ICombinationStrategy;
import index.girindex.combinationstrategy.combfamily.CombAnz;
import index.girindex.combinationstrategy.combfamily.CombMNZ;
import index.girindex.combinationstrategy.combfamily.CombMax;
import index.girindex.combinationstrategy.combfamily.CombMin;
import index.girindex.combinationstrategy.combfamily.CombSum;

public class CombinationStrategyFactory {

	public static ICombinationStrategy create(String combinationstrategy) {
		switch (combinationstrategy) {

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
