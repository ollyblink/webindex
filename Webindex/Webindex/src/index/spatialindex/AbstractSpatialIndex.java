package index.spatialindex;

import java.util.ArrayList;
import java.util.List;

import utils.dbcrud.DBDataManager;
import index.spatialindex.utils.SpatialIndexDocumentMetaData;
import index.spatialindex.utils.SpatialScoreTriple;
import index.utils.Document;

import com.vividsolutions.jts.index.quadtree.Quadtree;

public abstract class AbstractSpatialIndex implements ISpatialIndex {

	protected Quadtree quadTree;
	protected DBDataManager docProvider;
	protected Long[] docids;

	public AbstractSpatialIndex(Quadtree quadTree, DBDataManager docProvider, Long... docids) {
		this.quadTree = quadTree;
		this.docProvider = docProvider;
		this.docids = docids;
		fillQuadtree(docids);
	}

	@Override
	public void addLocations(SpatialIndexDocumentMetaData... dFPs) {
		if (dFPs == null || dFPs.length == 0) { // Bouncer
			return;
		} else {
			docProvider.storePersistently(dFPs);
			refillQuadtree(docids);
		}
	}

	

	protected void refillQuadtree(Long... docids) {
		this.quadTree = new Quadtree();
		fillQuadtree(docids);
	}

	protected abstract void fillQuadtree(Long... docids);


}
