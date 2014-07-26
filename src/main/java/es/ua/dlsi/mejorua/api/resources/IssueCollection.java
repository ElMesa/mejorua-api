package es.ua.dlsi.mejorua.api.resources;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.util.JSON;

@Path("/issues")
@Produces("application/json;charset=UTF-8")
public class IssueCollection {

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
	public Response get(@PathParam("id") String idString) {

		Response response;
		String json = "";
		String error = "Failed to retrieve resource";
		
		HashMap<Long, IssueBO> issues = IssueBO.getAll();

		if (issues != null) {
			json = JSON.encode(new ArrayList<IssueBO>(issues.values()));
			response = Response.ok(json).build();
		} else {
			response = Response.status(404).entity(error).type("text/plain").build();
		}

		return response;
	}
	
	@POST
	@Consumes("application/json;charset=UTF-8")
	public Response post(String resourceJSON) {

		Response response;
		String error = "No se ha podido crear/modificar el recurso";

		IssueBO issue = (IssueBO) JSON.decode(resourceJSON, IssueBO.class);

		if (issue != null) {
			IssueBO.add(issue);

			// TODO Componer la uri con la location del recurso creado

			// String baseUri = uri.getBaseUri().toString();
			// String resourceUri = baseUri + "incidencia/" +
			// incidencia.getId();
			// response = Response.created(resourceUri).build();
			response = Response.status(201).build();
		} else {
			response = Response.status(400).entity(error).type("text/plain").build();
		}

		return response;

	}
}
