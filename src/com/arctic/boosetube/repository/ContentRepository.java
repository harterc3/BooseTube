package com.arctic.boosetube.repository;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.arctic.boosetube.mapper.JSONMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ContentRepository implements IRepository {

	@Override
	public JSONObject read(String id) {
		DBCollection collection = null;

		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("test");
			collection = db.getCollection("content");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (collection == null)
			return null;

		BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
		DBObject result = collection.findOne(query);

		return JSONMapper.map(result);
	}

	@Override
	public JSONArray readAll() {
		DBCollection collection = null;

		try {
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("test");
			collection = db.getCollection("content");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (collection == null)
			return null;

		DBCursor cursor = collection.find().limit(10);

		return JSONMapper.map(cursor);
	}
}
