package com.arctic.boosetube.mapper;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class JSONMapper {
	public static JSONObject map(DBObject dbRecord) {
		JSONObject json = new JSONObject();
		for (String key : dbRecord.keySet()) {
			json.put(key, dbRecord.get(key));
		}
		return json;
	}

	public static JSONArray map(DBCursor dbCursor) {
		JSONArray result = new JSONArray();
		while (dbCursor.hasNext()) {
			DBObject record = dbCursor.next();
			result.put(map(record));
		}

		return result;
	}

	public static DBObject mapToDBObject(JSONObject json) {
		DBObject dbObject = new BasicDBObject();
		for (Object oKey : json.keySet()) {
			String key = (String) oKey;
			dbObject.put(key, json.get(key));
		}
		return dbObject;
	}
}
