package com.arctic.boosetube.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/upload")
public class UploadResource {

	final private String fileUploadPath = "C:\\uploads\\";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUpload(@QueryParam("filename") String filename)
			throws IOException {
		if (filename == null || filename.isEmpty())
			return Response.status(200).build();

		File file = new File(fileUploadPath, filename);
		if (!file.exists())
			return Response.noContent().build();

		String name = file.getName();

		JSONArray json = new JSONArray();
		json.put(buildJsonObject(name));

		return Response.status(200).entity(json.toString()).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postUpload(@Context final HttpServletRequest request)
			throws Exception {

		final ServletFileUpload upload = new ServletFileUpload();
		final FileItemIterator iter = upload.getItemIterator(request);

		JSONArray json = new JSONArray();

		while (iter.hasNext()) {
			final FileItemStream item = iter.next();

			String name = item.getName();
			final InputStream stream = item.openStream();
			String filepath = fileUploadPath.concat(name);
			writeToFile(stream, filepath);

			json.put(buildJsonObject(name));
		}
		return Response.status(200).entity(json.toString()).build();
	}

	@DELETE
	public Response deleteUpload(@QueryParam("filename") String filename) {
		if (filename == null || filename.isEmpty())
			return Response.notModified().build();

		File file = new File(fileUploadPath, filename);
		if (!file.exists())
			return Response.noContent().build();

		if (file.delete())
			return Response.ok().build();

		return Response.notModified().build();
	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) throws Exception {

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(uploadedFileLocation));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(out);
		}
	}

	void closeQuietly(FileOutputStream out) {
		try {
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JSONObject buildJsonObject(String name) throws IOException {
		String filepath = fileUploadPath.concat(name);
		BasicFileAttributes attr = Files.readAttributes(Paths.get(filepath),
				BasicFileAttributes.class);

		JSONObject jsono = new JSONObject();
		jsono.put("name", name);
		jsono.put("size", attr.size());
		jsono.put("url", "/rest/upload?filename=" + name);
		jsono.put("delete_url", "/rest/upload?filename=" + name);
		jsono.put("delete_type", "DELETE");

		return jsono;
	}
}
