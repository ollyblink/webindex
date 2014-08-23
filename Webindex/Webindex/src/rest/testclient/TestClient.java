package rest.testclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;

import utils.FilePathRetriever;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;

public class TestClient {
	public static void main(String[] args) throws FileNotFoundException, JAXBException, InstantiationException, IllegalAccessException, ClassNotFoundException {

//		String start = "C:/Users/rsp/Desktop/politics";
//		String start = "C:/Users/rsp/Desktop/inputdocuments";
		String start = "C:/Users/rsp/Desktop/hikrtexts";
		Client client = Client.create(new DefaultClientConfig());
		WebResource service = client.resource(getBaseURI());
		List<String> filePaths = new ArrayList<>();
		FilePathRetriever.INSTANCE.getFiles(new File(start), filePaths);
		int counter = 0;
		for (String file:filePaths) {
			counter++;
			if(counter == 200){
				return;
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				String text="", line=null;
				while((line=reader.readLine())!=null){
					text += line + " ";
				}
				reader.close();
				FormDataMultiPart part = new FormDataMultiPart().field("file", text);
				if(counter%999== 0){
					System.out.println("indexing document "+  (counter+1));
				}
				String response = service.path("docupload").type(MediaType.MULTIPART_FORM_DATA_TYPE).post(String.class, part);
				 
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// Ranking ranking = service.path("test pilot/intersection").accept(MediaType.APPLICATION_JSON).get(Ranking.class);
		//
		// System.out.println("Result: ");
		// for (IndexDocument doc : ranking.getResults()) {
		// System.out.println(doc);
		// }
		// System.out.println("Query was: " + ranking.getQuery());
		// System.out.println("result size: "+ ranking.getResults().size());
		//
		// System.out.println();
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8082/Webindex/webindex/index/").build();
	}
}
