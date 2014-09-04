package index.girindex.implementations;


public class TextPrimarySpatialIndex /*implements IGIRIndex*/ {
// 
//
//	@Override
//	public void addDocument(GIRDocument document) {
//		// TODO Auto-generated method stub
//		
//	}
////
////	/**
////	 * The actual index of this implementation An in memory index represented by a Map<K,V>.
////	 */
////	private Map<String /* Term */, Quadtree /* Spatial footprints */> index;
////	protected ITextInformationExtractor tokenizer;
////
////	public TextPrimarySpatialIndex(DBDataManager dbDataProvider, ITextInformationExtractor tokenizer, ICombinationStrategy combinationStrategy) {
////		super(dbDataProvider, combinationStrategy);
////		this.tokenizer = tokenizer;
////		fillInMemoryIndex();
////	}
////
////	private void fillInMemoryIndex() {
////		this.index = new HashMap<String, Quadtree>();
////		List<SimpleIndexDocument> documents = dbDataProvider.getDocTermLocationKeyValues();
////
////		// Inserting all documents into the in memory index
////		for (SimpleIndexDocument document : documents) {
////			Set<String> terms = document.getIndexTerms();
////			Set<Geometry> documentFootPrints = document.getDocumentFootPrints();
////			// Inserting all terms into the in memory inverted index
////			for (String term : terms) {
////				Quadtree tree = index.get(term);
////				if (tree == null) {
////					tree = new Quadtree();
////					index.put(term, tree);
////				}
////				for (Geometry documentFootPrint : documentFootPrints) {
////					tree.insert(documentFootPrint.getEnvelopeInternal(), document.getDocId());
////				}
////			}
////		}
////	}
////
//// 
////
////	@Override
////	public Ranking queryIndex(boolean isIntersected, TextIndexQuery textQuery, SpatialIndexQuery spatialQuery) {
////
////		// Get all the terms in the query
////		HashMap<Term, Integer> termFreqs = tokenizer.fullTransformation(textQuery.getTextQuery());
////
////		// Get all the documents' spatial indexes for those terms
////		HashMap<Term, Quadtree> documentTrees = new HashMap<>();
////		for (Term term : termFreqs.keySet()) {
////			Quadtree quadtree = index.get(term.getIndexedTerm());
////			if (quadtree != null) {
////				documentTrees.put(term, quadtree);
////			}
////		}
////
////		/*
////		 * Perform querying
////		 */
////		List<Ranking> spatialRankings = new ArrayList<Ranking>();
////		for (Term term : documentTrees.keySet()) {
////			Ranking spatialRanking = SpatialIndexUtils.performSpatialQuery(spatialQuery, documentTrees.get(term), dbDataProvider);
////			spatialRankings.add(spatialRanking);
////		}
////		// Now combine all the rankings and eliminate duplicated documents.
////		// Uses the maximum score of a document to determine its spatial score.
////		Ranking spatialRanking = combineRankings(spatialRankings);
////		Ranking textRanking = TextIndexUtils.performTextQuery(textQuery, tokenizer, dbDataProvider, spatialRanking.getResults());
////		/*
////		 * End querying
////		 */
////
////		// Last step: combine the ranks of the queries.
////		Ranking finalRanking = combinationStrategy.combineScores(isIntersected, textRanking, spatialRanking);
////
////		return finalRanking;
////	}
////
////	private Ranking combineRankings(List<Ranking> rankings) {
////		ArrayList<Score> finalScores = new ArrayList<Score>();
////
////		for (Ranking ranking : rankings) {
////			ArrayList<Score> toCompare = ranking.getResults();
////			for (Score score : toCompare) {
////				if (finalScores.contains(score)) {
////					Score finalScore = findScore(finalScores, score);
////					if (finalScore != null) {
////						finalScore.setScore(Math.max(finalScore.getScore(), score.getScore()));
////					}
////				} else {
////					finalScores.add(score);
////				}
////			}
////		}
////
////		return new Ranking(finalScores);
////	}
////
////	private Score findScore(ArrayList<Score> finalScores, Score score) {
////		for (Score finalScore : finalScores) {
////			if (score.equals(finalScore)) {
////				return finalScore;
////			}
////		}
////		return null;
////	}
//
//	@Override
//	public Ranking queryIndex(TextIndexQuery query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	 
//
//	@Override
//	public void clear() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public int N() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int ni(Term term) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public Iterable<Term> getAllTerms() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<Document> getDocumentsFor(Term term) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public HashMap<Term, List<Document>> getSubsetFor(List<Term> terms) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TextIndexMetaData getMetaData() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TextIndexMetaData getMetaData(Map<Term, List<Document>> docTerms) {
//		return 
//	}
//
//	@Override
//	public Ranking queryIndex(SpatialIndexQuery query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Ranking queryIndex(GIRQuery query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void setCombinationStrategy(ICombinationStrategy create) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	 

}
