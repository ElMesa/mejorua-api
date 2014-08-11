package es.ua.dlsi.mejorua.api.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;
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
	public Response get() {

		Response response;
		String json = "";
		String error = "Failed to retrieve resource";
		
		List<IssueTO> issues = IssueBO.getAll();

		if (issues != null) {
			json = JSON.encode(issues);
			response = Response.ok(json).build();
		} else {
			response = Response.status(404).entity(error).type("text/plain").build();
		}

		return response;
	}
	
	//TODO Use header/getParam with contentType application/vnd.geo+json to ask for geoJSON
	@Path("/issues.geojson")
	@GET
	public Response getGeoJSON() {

		Response response;

			response = Response.ok("GEOJSON").build();

		return response;
	}
	
	@POST
	@Consumes("application/json;charset=UTF-8")
	public Response post(String resourceJSON) {

		Response response;
		String error = "No se ha podido crear/modificar el recurso";

		IssueTO issue = (IssueTO) JSON.decode(resourceJSON, IssueTO.class);

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
