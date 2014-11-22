package es.ua.dlsi.mejorua.api.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/proxy")
@Produces(RESTAPI.contentTypeJSON)
public class Proxy {

	@Context
	UriInfo uri;

	/**
	 * Method processing HTTP GET requests, producing "application/json" MIME
	 * media type.
	 * 
	 * @return String that will be send back as a response of type
	 *         "application/json".
	 */
	@GET
	//@Produces(RESTAPI.contentTypeJSON)
	public Response get(@QueryParam("url") String urlEncoded) {

		Response response;
		String json = "";
		String error = "Failed to retrieve resource";

		URLConnection connection = null;
		String responseRawContent = null;
		InputStream remoteResponse = null;
		InputStream errorResponse = null;
				
		try {
			String urlDecoded = URLDecoder.decode(urlEncoded.replace("+", "%2B"), "UTF-8").replace("%2B", "+");
			connection = new URL(urlDecoded).openConnection();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			responseRawContent = "";
			while ((inputLine = in.readLine()) != null) 
				responseRawContent += inputLine;
			in.close();
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (responseRawContent != null) {
			//json = JSON.encode(responseRawContent);
			response = Response.ok(responseRawContent).build();
		} else {
			response = Response.status(404).entity("{ \"error\" : \"" + error + "\"}").type("text/plain")
					.build();
		}

		return response;
	}

}