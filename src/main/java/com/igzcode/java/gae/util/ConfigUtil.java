package com.igzcode.java.gae.util;

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.igzcode.java.gae.configuration.ConfigurationDto;
import com.igzcode.java.gae.configuration.ConfigurationManager;

/**
 * Utility for save/get/delete configuration values easily.
 * The values are readed from datastore and cached in memory.
 * The configurations can be public or private.
 */
public class ConfigUtil {
	
	static private boolean isDev;
	
	static private HashMap<String, String> allCachedValues;
	static private HashMap<String, String> publicCachedValues;
	
	static private ConfigurationManager configurationManager = ConfigurationManager.getInstance();
	
	static {
		String devPath = get("DEV_PATH");
		
		if ( devPath != null ) {
			String appPath = System.getProperty("user.dir");
			isDev = appPath.equals(devPath);
		}
		else {
			isDev = false;
			System.out.println("[Warning] DEV_PATH is not configurated ConfigUtil.IsDev() will be false");
		}
	}
	
	/**
	 * Get a configuration value as integer.
	 * @param p_key The configuration key
	 * @return The configuration value
	 */
	static public Integer getInteger ( String p_key ) {
		return Integer.valueOf( get(p_key) );
	}
	
	/**
	 * Get a configuration value.
	 * @param p_key The configuration key
	 * @return The configuration value
	 */
	static public String get ( String p_key ) {
		_checkCachedValues();
		return allCachedValues.get(p_key);
	}
	
	/**
	 * Create or update a configuration value.
	 * @param p_key The configuration key
	 * @param p_value The configuration value
	 * @param p_private The configuration scope
	 */
	static public void set ( String p_key, String p_value, Boolean p_private ) {
	    configurationManager.setValue(p_key, p_value, p_private);
	}
	
	/**
	 * Delete if exists a configuration value.
	 * @param p_key The configuration key
	 */
	static public void delete ( String p_key ) {
	    configurationManager.delete( p_key );
	}
	
	/**
	 * Use this to get public configuration values in JavaScript environment.
	 * @return A simple JSON with all public configurations values
	 */
	static public String getPublicConfigJSON () {
		_checkCachedValues();
		return new Gson().toJson(publicCachedValues);
	}

	static private void _checkCachedValues () {
		if ( allCachedValues == null ) {
			setCachedValues();
		}
	}
	
	/**
	 * Refresh the cached values.
	 */
	static public void setCachedValues () {
		publicCachedValues = new HashMap<String, String>();
		allCachedValues = new HashMap<String, String>();
		List<ConfigurationDto> configs = configurationManager.findAll();
		
		if ( configs != null && configs.size() > 0 ) {
			for ( ConfigurationDto config : configs ) {
				allCachedValues.put( config.getKeyId(), config.getValue() );
				
				if ( !config.isPrivate() ) {
					publicCachedValues.put( config.getKeyId(), config.getValue() );
				}
			}
		}
	}
	
	/**
	 * Indicate if the current environment is localhost.
	 * 
	 * @return A boolean that indicate if the current environment is localhost
	 */
	static public boolean isDev () {
		return isDev;
	}
}
