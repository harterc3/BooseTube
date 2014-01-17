package com.arctic.boosetube.resource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.json.JSONArray;
import org.json.JSONObject;

import com.arctic.boosetube.repository.ContentRepository;
import com.arctic.boosetube.repository.IRepository;
import com.arctic.boosetube.service.ContentService;

@Path("/content")
public class ContentResource {

	private final Cache contentCache = CacheManager.getInstance().getCache(
			"content");

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContentById(@PathParam("id") String id) {
		String cacheKey = String.format("content_%s", id);
		Element cacheElement = contentCache.get(cacheKey);

		if (cacheElement == null) {
			IRepository repository = new ContentRepository();
			JSONObject json = repository.read(id);
			String result = json.toString();
			contentCache.put(new Element(cacheKey, result));
			return Response.status(200).entity(result).build();
		} else {
			System.out.println("Retrieved cached element: "
					+ cacheElement.toString());
		}
		
		String cacheValue = (String) cacheElement.getObjectValue();
		return Response.status(200).entity(cacheValue).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContent(
			@DefaultValue("") @QueryParam(value = "type") final String type,
			@DefaultValue("") @QueryParam(value = "name") final String name) {
		String cacheKey = String.format("content_%s_%s", type, name);
		Element cacheElement = contentCache.get(cacheKey);

		if (cacheElement == null) {
			JSONArray json = null;
			if (type.isEmpty() && name.isEmpty()) {
				IRepository repository = new ContentRepository();
				json = repository.readAll();
			} else {
				json = ContentService.findByCriteria(type, name);
			}
			String result = json.toString();
			contentCache.put(new Element(cacheKey, result));
			return Response.status(200).entity(result).build();
		} else {
			System.out.println("Retrieved cached element: "
					+ cacheElement.toString());
		}
		String cacheValue = (String) cacheElement.getObjectValue();
		return Response.status(200).entity(cacheValue).build();
	}
}
