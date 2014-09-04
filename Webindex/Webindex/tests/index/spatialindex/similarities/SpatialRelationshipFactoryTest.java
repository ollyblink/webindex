package index.spatialindex.similarities;
import static org.junit.Assert.*;
import index.spatialindex.similarities.ISpatialRelationship;
import index.spatialindex.similarities.SpatialRelationshipFactory;
import index.spatialindex.similarities.pointsimilarities.in.InRelationship;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SpatialRelationshipFactoryTest {

	public static String[] relationships = {"point_in"}; 
	public static String[] simpleNames = {InRelationship.class.getSimpleName()};
	
	@Test
	public void testCreatedSimilarities() { 
			
		for(int i = 0; i< relationships.length;++i){
			assertTrue(SpatialRelationshipFactory.create(relationships[0]).getClass().getSimpleName().equals(simpleNames[0]));
		}
		 
	}

}
