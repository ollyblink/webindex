package index.spatialindex.utils;

import index.utils.DBManager;
import index.utils.IndexDocument;

import java.rmi.NoSuchObjectException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.postgis.PGgeometry;

import com.vividsolutions.jts.geom.Geometry;

public class IndexDocumentProvider {

	private DBManager dbManager;

	public IndexDocumentProvider(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	public ArrayList<IndexDocument> getDocumentIdAndFulltext(List<SpatialScoreTriple> ranking) {
		HashMap<Long, IndexDocument> indexDocuments = new HashMap<Long, IndexDocument>();
		try {
			PreparedStatement statement = dbManager.getConnection().prepareStatement("Select d.id, d.fulltext from documents d " + getWhereClause(ranking));
			ResultSet set = statement.executeQuery();
			while (set.next()) {
				long id = set.getLong(1);
				IndexDocument document = indexDocuments.get(id);
				if (document == null) {
					document = new IndexDocument(set.getLong(1), set.getString(2));
					indexDocuments.put(id, document);
				}
				for (SpatialScoreTriple data : ranking) {
					if (data.getDocid() == id) {
						document.getSpatialIndexDocumentMetaData().addSpatialScore(data.getGeometry(), data.getScore());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(indexDocuments.values());
	}

	private String getWhereClause(List<SpatialScoreTriple> dFPs) {

		if (dFPs == null || dFPs.size() == 0) {
			return ";";
		}
		int counter = 0;
		String whereClause = "where ";
		for (SpatialScoreTriple dFP : dFPs) {
			if (++counter == dFPs.size()) {
				whereClause += "d.id=" + dFP.getDocid() + ";";
			} else {
				whereClause += "d.id=" + dFP.getDocid() + " OR ";
			}
		}
		return whereClause;
	}

	private String getWhereClause(Long... ids) {
		if (ids == null || ids.length == 0) {
			return ";";
		}
		int counter = 0;
		String whereClause = "where ";
		for (Long id : ids) {
			if (++counter == ids.length) {
				whereClause += "d.id=" + id + ";";
			} else {
				whereClause += "d.id=" + id + " OR ";
			}
		}
		return whereClause;
	}

	/**
	 * this method returns all docs if the docids parameter is not specified or null. Else only returns the docs specified.
	 * 
	 * @param docids
	 * @return
	 */
	public List<SpatialIndexDocumentMetaData> getDocumentLocations(Long... docids) {
		if (dbManager.getConnector().tableExists("locations")) {
			String sql = "Select l.docid, l.geometry from locations l " + getWhereClause(docids);
			return new ArrayList<>(retrieveDocuments(sql).values());
		}
		return new ArrayList<>();
	}

	private Map<Long, SpatialIndexDocumentMetaData> retrieveDocuments(String sql) {
		Map<Long, SpatialIndexDocumentMetaData> docLocs = new HashMap<>();
		try {
			Statement statement = dbManager.getConnection().createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				long id = set.getLong(1);
				SpatialIndexDocumentMetaData docLoc = docLocs.get(id);
				if (docLoc == null) {
					docLoc = new SpatialIndexDocumentMetaData(set.getLong(1));
					docLocs.put(id, docLoc);
				}
				docLoc.addGeometry(GeometryConverter.convertPostGisToJTS((PGgeometry) set.getObject(2)));
			}
			statement.close();
		} catch (SQLException | NoSuchObjectException e) {
			e.printStackTrace();
		}
		return docLocs;
	}

	public void storePersistently(SpatialIndexDocumentMetaData... dFPs) {
		if (dFPs == null) {
			return;
		} else {
			if (dbManager.getConnector().tableExists("locations")) {
				String sql = "Insert into locations(docid, geometry) values (?,?);";
				try {
					PreparedStatement statement = dbManager.getConnection().prepareStatement(sql);
					for (SpatialIndexDocumentMetaData docLoc : dFPs) {
						List<Geometry> geometries = docLoc.getGeometries();
						for (Geometry geometry : geometries) {
							statement.setLong(1, docLoc.getDocid());
							statement.setObject(2, GeometryConverter.convertJTStoPostGis(geometry));
							statement.executeUpdate();
						}
					}
				} catch (SQLException | NoSuchObjectException e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("IndexDocumentProvider::storePersistently::could not insert, locations does not exist.");
			}
		}

	}

}
