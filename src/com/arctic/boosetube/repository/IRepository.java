package com.arctic.boosetube.repository;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IRepository {
	JSONObject read(String id);

	JSONArray readAll();
}
