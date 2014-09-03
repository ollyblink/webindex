package index.girindex.implementations;

import index.girindex.IGIRIndex;
import index.girindex.combinationstrategy.ICombinationStrategy;
import index.girindex.utils.GIRDocument;
import index.spatialindex.implementations.ISpatialIndex;
import index.textindex.implementations.ITextIndexNoInsertion;
import index.textindex.utils.Term;
import index.utils.Document;
import index.utils.Ranking;
import index.utils.RankingMetaData;
import index.utils.indexmetadata.TextIndexMetaData;
import index.utils.query.GIRQuery;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeparatedGIRIndex implements IGIRIndex {
	private ITextIndexNoInsertion textIndex;
	private ISpatialIndex spatialIndex;
	private ICombinationStrategy combinationStrategy;

	public SeparatedGIRIndex(ITextIndexNoInsertion textIndex, ISpatialIndex spatialIndex, ICombinationStrategy combinationStrategy) {
		this.textIndex = textIndex;
		this.spatialIndex = spatialIndex;
		this.combinationStrategy = combinationStrategy;
		
	}

	@Override
	public Ranking queryIndex(GIRQuery query) {
		System.out.println(query);
		Ranking textRanking = textIndex.queryIndex(query.getTextQuery());
		Ranking spatialRanking = spatialIndex.queryIndex(query.getSpatialQuery());
		Ranking combination = combinationStrategy.combineScores(query.isIntersected(), textRanking, spatialRanking);
		RankingMetaData meta = combination.getRankingMetaData();
		if(meta == null){
			meta = new RankingMetaData();
			combination.setRankingMetaData(meta);
		} 
		meta.setGIRQuery(query); 
		return combination;
	}

	@Override
	public void addDocument(GIRDocument document) {
	}

	@Override
	public Ranking queryIndex(TextIndexQuery query) {
		return this.textIndex.queryIndex(query);
	}
 
	@Override
	public void clear() {
		this.textIndex.clear();
		this.spatialIndex.clear();
	}

	@Override
	public int N() {
		return textIndex.N();
	}

	@Override
	public int ni(Term term) {
		return textIndex.ni(term);
	}

	@Override
	public Iterable<Term> getAllTerms() {
		return textIndex.getAllTerms();
	}

	@Override
	public List<Document> getDocumentsFor(Term term) {
		return textIndex.getDocumentsFor(term);
	}

	@Override
	public HashMap<Term, List<Document>> getSubsetFor(List<Term> terms) {
		return textIndex.getSubsetFor(terms);
	}

	@Override
	public TextIndexMetaData getMetaData() {
		return textIndex.getMetaData();
	}

	@Override
	public TextIndexMetaData getMetaData(Map<Term, List<Document>> docTerms) {
		return textIndex.getMetaData(docTerms);
	}

	@Override
	public Ranking queryIndex(SpatialIndexQuery query) {
		return this.spatialIndex.queryIndex(query);
	}

	public ITextIndexNoInsertion getTextIndex() {
		return textIndex;
	}

	public void setTextIndex(ITextIndexNoInsertion textIndex) {
		this.textIndex = textIndex;
	}

	public ISpatialIndex getSpatialIndex() {
		return spatialIndex;
	}

	public void setSpatialIndex(ISpatialIndex spatialIndex) {
		this.spatialIndex = spatialIndex;
	}

	public ICombinationStrategy getCombinationStrategy() {
		return combinationStrategy;
	}

	public void setCombinationStrategy(ICombinationStrategy combinationStrategy) {
		this.combinationStrategy = combinationStrategy;
	}

}
