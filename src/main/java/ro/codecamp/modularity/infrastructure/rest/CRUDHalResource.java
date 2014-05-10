package ro.codecamp.modularity.infrastructure.rest;

import java.net.URI;
import java.text.ParseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

public abstract class CRUDHalResource {

	@Context
	protected UriInfo uriInfo;

	protected JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	/**
	 * @return the path segment that uniquely identifies this collection
	 */
	protected abstract String getCollectionPath();

	@POST
	@Consumes(Constants.HAL_JSON_TYPE_STRING)
	public Response create(ReadableRepresentation representation)
			throws ParseException {
		Long id = doCreate(representation);
		return Response
				.status(201)
				.contentLocation(
						URI.create(uriInfo.getBaseUri().toString()
								+ getCollectionPath() + "/" + id)).build();
	}

	protected abstract Long doCreate(ReadableRepresentation representation);

	@GET
	@Path("{id}")
	public Response findById(@PathParam("id") Long id) {
		Representation result = doFindById(id);
		if (result == null) {
			result = halFactory
					.newRepresentation()
					.withProperty("title", "Unable to find item")
					.withProperty("code", "Error")
					.withProperty("message",
							"Item with id=" + id + " not found");
			return Response.status(404).entity(result).build();
		}
		return Response.ok(result).build();
	}

	protected abstract Representation doFindById(Long id);

	@PUT
	@Path("{id}")
	@Consumes(Constants.HAL_JSON_TYPE_STRING)
	public Response update(ReadableRepresentation representation,
			@PathParam("id") Long id) {
		Representation result = doUpdate(representation, id);
		return Response.ok(result).build();
	}

	protected abstract Representation doUpdate(
			ReadableRepresentation representation, Long id);

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") Long id) {
		boolean isDeleted = doDelete(id);
		if (isDeleted) {
			return Response.status(204).build();
		} else {
			Representation result = halFactory
					.newRepresentation()
					.withProperty("title", "Unable to delete item")
					.withProperty("code", "Error")
					.withProperty("message",
							"Item with id=" + id + " not found");
			return Response.status(404).entity(result).build();
		}
	}

	/**
	 * @return true if the resource is successfully removed
	 */
	protected abstract boolean doDelete(Long id);

}
