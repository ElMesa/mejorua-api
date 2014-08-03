package es.ua.dlsi.mejorua.api.resources;

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
import es.ua.dlsi.mejorua.api.business.IssueBO.State;
import es.ua.dlsi.mejorua.api.util.JSON;

/**
 * Example resource class hosted at the URI path "/myresource"
 */
@Path("/issues/{id}")
@Produces("application/json;charset=UTF-8")
public class IssueResource {

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

		IssueBO issue = IssueBO.get(Long.valueOf(idString));

		if (issue != null) {
			json = JSON.encode(issue);
			response = Response.ok(json).build();
		} else {
			response = Response.status(404).entity(error).type("text/plain")
					.build();
		}

		return response;
	}

	@POST
	@Consumes("application/json;charset=UTF-8")
	@Produces("*/*")
	public Response post(String resourceJSON, @PathParam("id") String idString) {

		Response response;
		String error = "No se ha podido crear/modificar el recurso";

		// IssueBO postedIssue = (IssueBO) JSON.decode(resourceJSON,
		// IssueBO.class);
		HashMap<String, Object> dataHash = JSON.decodeToHash(resourceJSON);

		if (dataHash.size() > 0) {

			boolean isChanged = false;

			IssueBO issue = IssueBO.get(new Long(idString));

			String state = (String) dataHash.get("state");
			if (state != null) {
				if (issue.onChangeState(State.valueOf(state))) {
					isChanged = true;
				}
			}

			if (isChanged) {
				issue.update();
			}

			String JSONResponse = JSON.encode(issue);

			// TODO Componer la uri con la location del recurso creado

			// String baseUri = uri.getBaseUri().toString();
			// String resourceUri = baseUri + "incidencia/" +
			// incidencia.getId();
			// response = Response.created(resourceUri).build();
			response = Response.status(201).entity(JSONResponse).build();
		} else {
			response = Response.status(400).entity(error).type("text/plain")
					.build();
		}

		return response;

	}

	// TODO Solucionar error de unsuported media type al mandar JSON
	/*
	 * @PUT
	 * 
	 * @Consumes("application/json;charset=UTF-8") public Response put(String
	 * resourceJSON) {
	 * 
	 * Response response; String error =
	 * "No se ha podido crear/modificar el recurso";
	 * 
	 * IssueBO incidencia = IssueBO.newFromJSON(resourceJSON);
	 * 
	 * if (incidencia != null) { IssueDAO.save(incidencia);
	 * 
	 * // TODO Componer la uri con la location del recurso creado
	 * 
	 * // String baseUri = uri.getBaseUri().toString(); // String resourceUri =
	 * baseUri + "incidencia/" + // incidencia.getId(); // response =
	 * Response.created(resourceUri).build(); response =
	 * Response.status(201).build(); } else { response =
	 * Response.status(400).entity(error).type("text/plain").build(); }
	 * 
	 * return response;
	 * 
	 * }
	 */

}
