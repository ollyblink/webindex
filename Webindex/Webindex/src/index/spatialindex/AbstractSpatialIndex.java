package index.spatialindex;

import index.spatialindex.utils.IndexDocumentProvider;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialScoreTriple;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public abstract class AbstractSpatialIndex implements ISpatialIndex {
 
	protected Quadtree quadTree;
	protected IndexDocumentProvider docProvider;

	public AbstractSpatialIndex(Quadtree quadTree, IndexDocumentProvider docProvider) {
		this.quadTree = quadTree;
		this.docProvider = docProvider;
		fillQuadtree();
	}

	private void fillQuadtree() {
		List<SpatialIndexDocumentMetaData> docLocs = docProvider.getDocumentLocations();
		for (SpatialIndexDocumentMetaData docLoc : docLocs) {
			List<Geometry> geometries = docLoc.getGeometries();
			for(Geometry geometry: geometries){
				quadTree.insert(geometry.getEnvelopeInternal(), new SpatialScoreTriple(docLoc.getDocid(), geometry, 0f));
			}
		}
	}

	
	protected void refillQuadtree(){
		this.quadTree = new Quadtree();
		fillQuadtree();
	}
	 

}
