package index.textindex.similarities.booleanmodels;

import static org.junit.Assert.assertEquals;
import index.textindex.implementations.RAMTextOnlyIndex;
import index.textindex.utils.Term;
import index.textindex.utils.informationextractiontools.MockTextInformationExtractor;
import index.utils.Document;
import index.utils.Score;
import index.utils.query.TextIndexQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import rest.dao.RESTScore;
import rest.dao.Ranking;

public class SimpleBooleanSimilarityTest {

	@Test
	public void testCalculateSimilarity() {

		SimpleBooleanSimilarity similarity = new SimpleBooleanSimilarity();
		HashMap<Term, List<Document>> documentTerms = new HashMap<>();

		Term[] terms = new Term[14];
		terms[0] = new Term("to");
		terms[1] = new Term("do");
		terms[2] = new Term("is");
		terms[3] = new Term("be");
		terms[4] = new Term("or");
		terms[5] = new Term("not");
		terms[6] = new Term("I");
		terms[7] = new Term("am");
		terms[8] = new Term("what");
		terms[9] = new Term("think");
		terms[10] = new Term("therefore");
		terms[11] = new Term("da");
		terms[12] = new Term("let");
		terms[13] = new Term("it");

		for (Term term : terms) {
			documentTerms.put(term, new ArrayList<Document>());
		}

		Document[] documents = new Document[4];
		documents[0] = new Document(1l);
		documents[1] = new Document(2l);
		documents[2] = new Document(3l);
		documents[3] = new Document(4l);

		documentTerms.get(terms[0]).add(documents[0]);
		documentTerms.get(terms[1]).add(documents[0]);
		documentTerms.get(terms[2]).add(documents[0]);

		documentTerms.get(terms[0]).add(documents[1]);
		documentTerms.get(terms[4]).add(documents[1]);
		documentTerms.get(terms[5]).add(documents[1]);
		documentTerms.get(terms[6]).add(documents[1]);
		documentTerms.get(terms[7]).add(documents[1]);
		documentTerms.get(terms[8]).add(documents[1]);

		documentTerms.get(terms[1]).add(documents[2]);
		documentTerms.get(terms[6]).add(documents[2]);
		documentTerms.get(terms[7]).add(documents[2]);
		documentTerms.get(terms[9]).add(documents[2]);
		documentTerms.get(terms[10]).add(documents[2]);

		documentTerms.get(terms[1]).add(documents[3]);
		documentTerms.get(terms[11]).add(documents[3]);
		documentTerms.get(terms[12]).add(documents[3]);
		documentTerms.get(terms[13]).add(documents[3]);

		HashMap<Term, List<Document>> relevantDocuments = new HashMap<Term, List<Document>>();
		relevantDocuments.put(terms[0], documentTerms.get(terms[0]));
		relevantDocuments.put(terms[1], documentTerms.get(terms[1]));

		RAMTextOnlyIndex index = new RAMTextOnlyIndex(documentTerms, null, new MockTextInformationExtractor());

		// With intersection (AND)
		Ranking hits = index.queryIndex(new TextIndexQuery("to do", "simpleboolean", true));
		HashMap<Long, Float> values = new HashMap<Long, Float>();
		values.put(1l, 1f);

		for (RESTScore res : hits.getResults()) {
			assertEquals(values.get(res.getDocument().getId().getId()), res.getScore(), 0.001f);
		}

		// With union (OR)
		hits = index.queryIndex(new TextIndexQuery("to do", "simpleboolean", false));
		values = new HashMap<Long, Float>();
		values.put(1l, 1f);
		values.put(2l, 1f);
		values.put(3l, 1f);
		values.put(4l, 1f);

		for (RESTScore res : hits.getResults()) {
			assertEquals(values.get(res.getDocument().getId().getId()), res.getScore(), 0.001f);
		}
	}

}
