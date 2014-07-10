package es.ua.dlsi.mejorua.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.ua.dlsi.mejorua.api.business.IncidenciaBO;
import es.ua.dlsi.mejorua.api.persistance.IncidenciaDAO;

/**
 * Example resource class hosted at the URI path "/myresource"
 */
@Path("/incidencia/{id}")
@Produces("application/json; charset=UTF-8")
public class IncidenciaREST {

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
		String JSON = "";
		String error = "No se ha encontrado el recurso.";

		/*
		 * IncidenciaBO incidencia = new IncidenciaBO();
		 * 
		 * incidencia.setId(Long.valueOf(idString)); incidencia.setLatitud(1);
		 * incidencia.setLongitud(2); incidencia.setTermino("Termino");
		 * incidencia.setAccion("Acción");
		 */

		// Poblamos "incidencias" con una incidencia 1
		if (IncidenciaDAO.get(1) == null) {
			IncidenciaBO incidenciaPrePoblada = new IncidenciaBO();
			incidenciaPrePoblada.setId(1);
			incidenciaPrePoblada.setLatitud(1);
			incidenciaPrePoblada.setLongitud(1);
			incidenciaPrePoblada.setTermino("Termino pre poblado");
			incidenciaPrePoblada.setAccion("Acción pre poblada");
			
			IncidenciaDAO.save(incidenciaPrePoblada);
		}
		
		IncidenciaBO incidencia = IncidenciaDAO.get(Long.valueOf(idString));

		if (incidencia != null) {
			JSON = incidencia.toJSON();
			response = Response.ok(JSON).build();
		} else {
			response = Response.status(404).entity(error).type("text/plain").build();
		}

		return response;
	}

	//TODO Solucionar error de unsuported media type al mandar JSON
	@PUT
	@Consumes("application/json; charset=UTF-8")
	public Response put(String resourceJSON) {

		Response response;
		String error = "No se ha podido crear/modificar el recurso";

		IncidenciaBO incidencia = IncidenciaBO.newFromJSON(resourceJSON);

		if (incidencia != null) {
			IncidenciaDAO.save(incidencia);

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
	
	//TODO BORRAR ESTO - COPY PASTE DEL @PUT PARA TESTEAR CHROME BUG FORBIDDEN ON PUT
	@POST
	@Consumes("application/json; charset=UTF-8")
	public Response post(String resourceJSON) {

		Response response;
		String error = "No se ha podido crear/modificar el recurso";

		IncidenciaBO incidencia = IncidenciaBO.newFromJSON(resourceJSON);

		if (incidencia != null) {
			IncidenciaDAO.save(incidencia);

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
