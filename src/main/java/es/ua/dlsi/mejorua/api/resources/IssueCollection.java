package es.ua.dlsi.mejorua.api.resources;

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
@Produces(RESTAPI.contentTypeJSON)
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
	@Consumes(RESTAPI.contentTypeJSON)
	public Response post(String resourceJSON) {

		Response response;
		String error = "";
		long issueId;

		IssueTO issueTO = (IssueTO) JSON.decode(resourceJSON, IssueTO.class);
		IssueBO issueBO = IssueBO.newFromPartialIssueTO(issueTO);

		if (issueBO != null) {
			issueId = IssueBO.add(issueBO.getTO());
			
			if(issueId >= 0) {

			// TODO Componer la uri con la location del recurso creado

			// String baseUri = uri.getBaseUri().toString();
			// String resourceUri = baseUri + "incidencia/" +
			// incidencia.getId();
			// response = Response.created(resourceUri).build();
			response = Response.status(201).build();
			} else {
				error = "Server could not persist the issue";
				response = Response.status(500).entity(error).type("text/plain").build();
			}
		} else {
			error = "Server could not decode the JSON issue";
			response = Response.status(400).entity(error).type("text/plain").build();
		}

		return response;

	}
}
