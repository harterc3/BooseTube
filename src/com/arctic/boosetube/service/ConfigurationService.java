package com.arctic.boosetube.service;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class ConfigurationService {
	private XMLConfiguration _config = null;

	public ConfigurationService() {
		try {
			_config = new XMLConfiguration(
					"C:\\Users\\Fury\\git\\BooseTube\\configuration.xml");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getString(String key) {
		if (_config == null)
			return null;
		return _config.getString(key);
	}

	public int getInteger(String key) {
		if (_config == null)
			return 0;
		return _config.getInt(key);
	}
}
