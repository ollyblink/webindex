package index.spatialindex.implementations;

import index.spatialindex.AbstractSpatialIndex;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialIndexMetaData;
import index.spatialindex.utils.SpatialIndexUtils;
import index.spatialindex.utils.SpatialScoreTriple;
import index.utils.DBDataProvider;
import index.utils.Ranking;
import index.utils.query.SpatialIndexQuery;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class SpatialOnlyIndex extends AbstractSpatialIndex {

	public SpatialOnlyIndex(Quadtree quadTree, DBDataProvider docProvider, Long... docids) {
		super(quadTree, docProvider, docids);
	}

	 
	@Override
	public Ranking queryIndex(SpatialIndexQuery query) {

		return SpatialIndexUtils.performSpatialQuery(query, quadTree,docProvider);
	}


	

	

	@Override
	public SpatialIndexMetaData getSpatialMetaData() {
		//TODO 
		throw new NoSuchMethodError();
	}

	@Override
	protected void fillQuadtree(Long... docids) {
		List<SpatialIndexDocumentMetaData> docLocs = docProvider.getDocumentLocations(docids);
		for (SpatialIndexDocumentMetaData docLoc : docLocs) {
			List<Geometry> geometries = docLoc.getGeometries();
			for (Geometry geometry : geometries) {
				quadTree.insert(geometry.getEnvelopeInternal(), new SpatialScoreTriple(docLoc.getDocid(), geometry, 0f));
			}
		}
	}


	@Override
	public void refill() {
		refillQuadtree();
	}

}
