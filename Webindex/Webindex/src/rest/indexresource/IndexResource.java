package rest.indexresource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rest.dao.IndexContainer;
import rest.dao.IndexDao;
import rest.dao.Ranking;

@Path("/index")
public class IndexResource {

	// @POST
	// @Path("/docupload")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response postDocument(@FormDataParam("file") String text, @FormDataParam("file") FormDataContentDisposition fileHeader) {
	// if (text != null && text.length() > 0) {
	// IndexDao.INSTANCE.addDocument(text);
	// return Response.status(204).entity("Indexing success: could upload file.").build();
	// } else {
	// return Response.status(200).entity("Could not index file \"" + fileHeader.getFileName() + "\". No content found! File was empty.").build();
	// }
	// }

	@GET
	@Path("/{textsimilaritytype}/{spatialrelationship}/{locationquery}/{textquery}/{textintersected}/{textspatialintersected}/{combinationstrategy}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking query(@PathParam("textsimilaritytype") String textsimilaritytype, @PathParam("spatialrelationship") String spatialrelationship, @PathParam("locationquery") String locationquery, @PathParam("textquery") String textquery, @PathParam("textintersected") String textintersected, @PathParam("textspatialintersected") String textspatialintersected, @PathParam("combinationstrategy") String combinationstrategy) {
		System.out.println("INDEXRESOURCE::query:: received: (" + textsimilaritytype + "," + spatialrelationship + "," + locationquery + "," + textquery + "," + textintersected + "," + textspatialintersected + "," + combinationstrategy + ")");
		Ranking ranking = IndexDao.INSTANCE.submitQuery(textsimilaritytype.trim(), spatialrelationship.trim(), locationquery.trim(), textquery.trim(), textintersected.trim(), textspatialintersected.trim(), combinationstrategy.trim());
//		for(RESTScore s: ranking.getResults()){
//			System.out.println(s.getDocument().getId() + ": "+ s.getScore());
//		}
		return ranking;
	}

	@GET
	@Path("/getindex")
	@Produces(MediaType.APPLICATION_JSON)
	public IndexContainer getIndex() {
		return IndexDao.INSTANCE.getIndex();
	}
	
	@GET
	@Path("/testrest")
	@Produces(MediaType.TEXT_PLAIN)
	public String testRest() {
		return "works";
	}

	@GET
	@Path("/cleardb")
	@Produces(MediaType.TEXT_PLAIN)
	public String cleardb() {
		IndexDao.INSTANCE.dropAndInitializeTables();
		return "cleared db";
	}

}
