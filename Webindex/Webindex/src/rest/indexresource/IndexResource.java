package rest.indexresource;

import index.utils.Ranking;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import rest.dao.IndexDao;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/index")
public class IndexResource {

	@POST
	@Path("/docupload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postDocument(@FormDataParam("file") String text, @FormDataParam("file") FormDataContentDisposition fileHeader) { 
		if (text != null && text.length() > 0) {
			IndexDao.INSTANCE.addDocument(text); 
			return Response.status(204).entity("Indexing success: could upload file.").build();
		} else {
			return Response.status(200).entity("Could not index file \"" + fileHeader.getFileName() + "\". No content found! File was empty.").build();
		}
	}

	@GET
	@Path("/cosine1/{query}/{intersected}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking cosine1Query(@PathParam("query") String query, @PathParam("intersected") String intersected) {
		return sendQuery(query, "cosine1",intersected);
	}
	@GET
	@Path("/cosine2/{query}/{intersected}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking cosine2Query(@PathParam("query") String query, @PathParam("intersected") String intersected) {
		return sendQuery(query, "cosine2",intersected);
	}
	@GET
	@Path("/cosine3/{query}/{intersected}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking cosine3Query(@PathParam("query") String query, @PathParam("intersected") String intersected) {
		return sendQuery(query, "cosine3",intersected);
	}

	@GET
	@Path("/bm1/{query}/{intersected}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking bm1Query(@PathParam("query") String query, @PathParam("intersected") String intersected) {
		return sendQuery(query, "bm1",intersected);
	}

	@GET
	@Path("/bm11/{query}/{intersected}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking bm11Query(@PathParam("query") String query, @PathParam("intersected") String intersected) {
		return sendQuery(query, "bm11",intersected);
	}

	@GET
	@Path("/bm15/{query}/{intersected}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking bm15Query(@PathParam("query") String query, @PathParam("intersected") String intersected) {
		return sendQuery(query, "bm15",intersected);
	}

	@GET
	@Path("/bm25/{query}/{intersected}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ranking bm25Query(@PathParam("query") String query, @PathParam("intersected") String intersected) {
		return sendQuery(query, "bm25",intersected);
	}

	private Ranking sendQuery(String query, String textSimilarityType, String intersected) {
		System.out.println("received : " + query);
		Ranking ranking =IndexDao.INSTANCE.submitQuery(textSimilarityType, query, intersected);
//		System.out.println(ranking);
		return ranking;
	}
	
	@GET
	@Path("/testrest")
	@Produces(MediaType.TEXT_PLAIN)
	public String testRest(){
		return "works";
	}
	
	@GET
	@Path("/cleardb")
	@Produces(MediaType.TEXT_PLAIN)
	public String cleardb(){ 
		IndexDao.INSTANCE.dropAndInitializeTables();
		return "cleared db";
	}

}
