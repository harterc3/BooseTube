package com.arctic.boosetube.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.arctic.boosetube.model.FileMeta;
import com.arctic.boosetube.repository.ContentRepository;
import com.arctic.boosetube.repository.IRepository;
import com.arctic.boosetube.service.ConfigurationService;
import com.arctic.boosetube.service.ContentService;
import com.arctic.boosetube.util.StringUtil;

@Path("/upload")
public class UploadResource {

	private String fileUploadPath = null;

	public UploadResource() {
		ConfigurationService configService = new ConfigurationService();
		fileUploadPath = configService.getString("file-upload.path");
	}

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
		json.put(buildResponseJsonObject(name,
				filename.substring(0, filename.indexOf('.') - 1)));

		return Response.status(200).entity(json.toString()).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postUpload(@Context final HttpServletRequest request)
			throws Exception {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		if (isMultipart) {
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold(1000000);
			factory.setRepository(new File(this.fileUploadPath));

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Set overall request size constraint
			upload.setSizeMax(1000000);

			// Parse the request
			List<FileItem> items = upload.parseRequest(request);

			// Process the uploaded items
			Iterator<FileItem> iter = items.iterator();
			JSONArray json = new JSONArray();
			FileMeta filemeta = new FileMeta();
			while (iter.hasNext()) {
				FileItem item = iter.next();

				String itemName = item.getFieldName();
				final InputStream stream = item.getInputStream();

				switch (itemName) {
				case "title":
					filemeta.setTitle(StringUtil
							.getStringFromInputStream(stream));
					break;
				case "description":
					filemeta.setDescription(StringUtil
							.getStringFromInputStream(stream));
					break;
				case "keywords":
					String allWords = StringUtil
							.getStringFromInputStream(stream);
					String[] keywords = allWords.split(",");
					for (int i = 0; i < keywords.length; i++) {
						String temp = keywords[i];
						keywords[i] = temp.trim();
					}
					filemeta.setKeywords(keywords);
					break;
				default:
					String ext = FilenameUtils.getExtension(item.getName());
					String oid = ObjectId.get().toString();
					filemeta.setId(oid);
					String name = String.format("%s.%s", oid, ext);
					String filepath = fileUploadPath.concat(name);
					filemeta.setPath(filepath);
					writeToFile(stream, filepath);
					File file = new File(fileUploadPath, name);
					if (!file.exists())
						return Response.noContent().build();

					String mimeType = Files.probeContentType(Paths
							.get(filepath));
					if (mimeType.startsWith("image")) {
						filemeta.setType(FileMeta.FileType.Image);
					} else if (mimeType.startsWith("video")) {
						filemeta.setType(FileMeta.FileType.Video);
					} else if (mimeType.startsWith("video")) {
						filemeta.setType(FileMeta.FileType.Audio);
					} else {
						filemeta.setType(FileMeta.FileType.Unknown);
					}

					json.put(buildResponseJsonObject(name, oid));
					break;
				}
			}
			ContentService.createObject(filemeta);
			return Response.status(200).entity(json.toString()).build();
		}
		return Response.notModified().build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteUpload(@PathParam("id") String id) {
		if (id == null || id.isEmpty())
			return Response.notModified().build();
		IRepository repository = new ContentRepository();
		String filepath = (String) repository.read(id).get("filepath");
		File file = new File(filepath);
		if (!file.exists())
			return Response.noContent().build();

		if (file.delete()) {
			if (repository.delete(id))
				return Response.ok().build();
		}

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

	private void closeQuietly(FileOutputStream out) {
		try {
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JSONObject buildResponseJsonObject(String name, String oid)
			throws IOException {
		String filepath = fileUploadPath.concat(name);
		BasicFileAttributes attr = Files.readAttributes(Paths.get(filepath),
				BasicFileAttributes.class);

		JSONObject jsono = new JSONObject();
		jsono.put("name", name);
		jsono.put("size", attr.size());
		jsono.put("url", fileUploadPath.concat(name));
		jsono.put("delete_url", "/rest/upload/".concat(oid));
		jsono.put("delete_type", "DELETE");

		return jsono;
	}
}
