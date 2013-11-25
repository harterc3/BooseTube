package com.arctic.boosetube.mapper;

import org.json.JSONArray;
import org.json.JSONObject;

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
}
