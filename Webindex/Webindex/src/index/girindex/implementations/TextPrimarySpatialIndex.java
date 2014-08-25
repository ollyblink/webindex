package index.girindex.implementations;

import index.girindex.IGIRIndex;
import index.textindex.utils.texttransformation.ITextTokenizer;
import index.utils.DBDataProvider;
import index.utils.Ranking;
import index.utils.SimpleIndexDocument;
import index.utils.query.SpatialIndexQuery;
import index.utils.query.TextIndexQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class TextPrimarySpatialIndex implements IGIRIndex{

	/**
	 * The actual index of this implementation An in memory index represented by a Map<K,V>.
	 */
	private Map<String /* Term */, Quadtree /* Spatial footprints */> index;
	
	private DBDataProvider dbDataProvider; 

	public TextPrimarySpatialIndex(DBDataProvider dbDataProvider, ITextTokenizer tokenizer) {
		this.dbDataProvider = dbDataProvider; 
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
	public Ranking queryIndex(TextIndexQuery textQuery, SpatialIndexQuery spatialQuery) {
		// TODO Auto-generated method stub
		return null;
	}

}
