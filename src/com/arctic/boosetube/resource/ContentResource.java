package com.arctic.boosetube.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.arctic.boosetube.repository.ContentRepository;
import com.arctic.boosetube.repository.IRepository;

@Path("/content")
public class ContentResource {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContent(@PathParam("id") String id) {
		IRepository repository = new ContentRepository();
		JSONObject json = repository.read(id);
		return Response.status(200).entity(json.toString()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContent() {
		IRepository repository = new ContentRepository();
		JSONArray json = repository.readAll();
		return Response.status(200).entity(json.toString()).build();
	}
}
