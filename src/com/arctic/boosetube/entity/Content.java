package com.arctic.boosetube.entity;

import org.json.JSONObject;

public class Content extends JSONObject implements IEntity {
	public String id;
	public String name;
	public String text;
	
	public Content() {
		id = "";
		name = "";
		text = "";
	}
}
