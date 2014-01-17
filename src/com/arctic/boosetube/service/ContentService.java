package com.arctic.boosetube.service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.arctic.boosetube.mapper.FileMetaMapper;
import com.arctic.boosetube.mapper.JSONMapper;
import com.arctic.boosetube.model.FileMeta;
import com.arctic.boosetube.repository.ContentRepository;
import com.arctic.boosetube.repository.IRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ContentService {
	public static JSONArray findByCriteria(String type, String name) {
		DBObject query = new BasicDBObject();
		if (!type.equals(""))
			query.put("type", type);
		if (!name.equals(""))
			query.put("name", name);

		IRepository repository = new ContentRepository();
		return repository.read(query);
	}
	
	public static String createObject(JSONObject json) {
		IRepository repository = new ContentRepository();
		return repository.create(JSONMapper.mapToDBObject(json));
	}
	
	public static String createObject(FileMeta fileMeta) {
		IRepository repository = new ContentRepository();
		return repository.create(FileMetaMapper.map(fileMeta));
	}
}
