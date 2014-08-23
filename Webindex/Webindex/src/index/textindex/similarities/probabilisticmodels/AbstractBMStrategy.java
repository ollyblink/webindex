package index.textindex.similarities.probabilisticmodels;

public abstract class AbstractBMStrategy  implements IBMStrategy{
	protected float log(float value) {
		return (float) (Math.log(value) / Math.log(2));
	}
}
