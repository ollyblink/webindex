package index.girindex.implementations;

import index.girindex.AbstractGIRIndex;
import index.girindex.combinationstrategy.ICombinationStrategy;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialIndexMetaData;
import index.spatialindex.utils.SpatialIndexUtils;
import index.textindex.utils.Term;
import index.textindex.utils.TextIndexMetaData;
import index.textindex.utils.TextIndexUtils;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.Ranking;
import index.utils.Score;
import index.utils.SimpleIndexDocument;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.dbcrud.DBDataManager;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class TextPrimarySpatialIndex extends AbstractGIRIndex {

	/**
	 * The actual index of this implementation An in memory index represented by a Map<K,V>.
	 */
	private Map<String /* Term */, Quadtree /* Spatial footprints */> index;
	protected ITextTokenizer tokenizer;

	public TextPrimarySpatialIndex(DBDataManager dbDataProvider, ITextTokenizer tokenizer, ICombinationStrategy combinationStrategy) {
		super(dbDataProvider, combinationStrategy);
		this.tokenizer = tokenizer;
		fillInMemoryIndex();
	}

	private void fillInMemoryIndex() {
		this.index = new HashMap<String, Quadtree>();
		List<SimpleIndexDocument> documents = dbDataProvider.getDocTermLocationKeyValues();

		// Inserting all documents into the in memory index
		for (SimpleIndexDocument document : documents) {
			Set<String> terms = document.getIndexTerms();
			Set<Geometry> documentFootPrints = document.getDocumentFootPrints();
			// Inserting all terms into the in memory inverted index
			for (String term : terms) {
				Quadtree tree = index.get(term);
				if (tree == null) {
					tree = new Quadtree();
					index.put(term, tree);
				}
				for (Geometry documentFootPrint : documentFootPrints) {
					tree.insert(documentFootPrint.getEnvelopeInternal(), document.getDocId());
				}
			}
		}
	}

	@Override
	public void addDocument(String text) {
		dbDataProvider.addDocument(text);
		fillInMemoryIndex();
	}

	@Override
	public void addDocuments(List<String> texts) {
		dbDataProvider.addDocuments(texts);
		fillInMemoryIndex();
	}

	@Override
	public Ranking queryIndex(boolean isIntersected, TextIndexQuery textQuery, SpatialIndexQuery spatialQuery) {

		// Get all the terms in the query
		HashMap<Term, Integer> termFreqs = tokenizer.transform(textQuery.getTextQuery());

		// Get all the documents' spatial indexes for those terms
		HashMap<Term, Quadtree> documentTrees = new HashMap<>();
		for (Term term : termFreqs.keySet()) {
			Quadtree quadtree = index.get(term.getIndexedTerm());
			if (quadtree != null) {
				documentTrees.put(term, quadtree);
			}
		}

		/*
		 * Perform querying
		 */
		List<Ranking> spatialRankings = new ArrayList<Ranking>();
		for (Term term : documentTrees.keySet()) {
			Ranking spatialRanking = SpatialIndexUtils.performSpatialQuery(spatialQuery, documentTrees.get(term), dbDataProvider);
			spatialRankings.add(spatialRanking);
		}
		// Now combine all the rankings and eliminate duplicated documents.
		// Uses the maximum score of a document to determine its spatial score.
		Ranking spatialRanking = combineRankings(spatialRankings);
		Ranking textRanking = TextIndexUtils.performTextQuery(textQuery, tokenizer, dbDataProvider, spatialRanking.getResults());
		/*
		 * End querying
		 */

		// Last step: combine the ranks of the queries.
		Ranking finalRanking = combinationStrategy.combineScores(isIntersected, textRanking, spatialRanking);

		return finalRanking;
	}

	private Ranking combineRankings(List<Ranking> rankings) {
		ArrayList<Score> finalScores = new ArrayList<Score>();
		
		for(Ranking ranking: rankings){
			ArrayList<Score> toCompare = ranking.getResults();
			for(Score score: toCompare){
				if(finalScores.contains(score)){
					Score finalScore = findScore(finalScores, score);
					if(finalScore != null){
						finalScore.setScore(Math.max(finalScore.getScore(), score.getScore()));
					}
				}else{
					finalScores.add(score);
				}
			}
		}
		
		return new Ranking(finalScores);
	}

	private Score findScore(ArrayList<Score> finalScores, Score score) {
		for(Score finalScore: finalScores){
			if(score.equals(finalScore)){
				return finalScore;
			}
		}
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ITextTokenizer getTokenizer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextIndexMetaData getTextMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ranking queryIndex(TextIndexQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLocations(SpatialIndexDocumentMetaData... documentFootPrints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SpatialIndexMetaData getSpatialMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ranking queryIndex(SpatialIndexQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

}
