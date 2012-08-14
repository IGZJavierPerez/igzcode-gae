package com.igzcode.java.gae.util;

import java.util.HashMap;
import java.util.List;

import com.igzcode.java.gae.configuration.ConfigurationDto;
import com.igzcode.java.gae.configuration.ConfigurationManager;
import com.igzcode.java.gae.serialization.DataTableList;

/**
 * Utility for save/get/delete configuration values easily.
 * The values are readed from datastore and cached in memory.
 * The configurations can be public or private.
 */
public class ConfigUtil {
	
	static private boolean _IsDev;
	
	static private HashMap<String, String> _AllCachedValues;
	static private HashMap<String, String> _PublicCachedValues;
	
	static {
		String devPath = Get("DEV_PATH");
		
		if ( devPath != null ) {
			String appPath = System.getProperty("user.dir");
			_IsDev = appPath.equals(devPath);
		}
		else {
			_IsDev = false;
			System.out.println("[Warning] DEV_PATH is not configurated ConfigUtil.IsDev() will be false");
		}
	}
	
	/**
	 * Get a configuration value as integer.
	 * @param p_key The configuration key
	 * @return The configuration value
	 */
	static public Integer GetInteger ( String p_key ) {
		return Integer.valueOf( Get(p_key) );
	}
	
	/**
	 * Get a configuration value.
	 * @param p_key The configuration key
	 * @return The configuration value
	 */
	static public String Get ( String p_key ) {
		_CheckCachedValues();
		return _AllCachedValues.get(p_key);
	}
	
	/**
	 * Create or update a configuration value.
	 * @param p_key The configuration key
	 * @param p_value The configuration value
	 * @param p_private The configuration scope
	 */
	static public void Set ( String p_key, String p_value, Boolean p_private ) {
		new ConfigurationManager().SetValue(p_key, p_value, p_private);
	}
	
	/**
	 * Delete if exists a configuration value.
	 * @param p_key The configuration key
	 */
	static public void Delete ( String p_key ) {
		new ConfigurationManager().Delete( p_key );
	}
	
	/**
	 * Use this to get public configuration values in JavaScript environment.
	 * @return A simple JSON with all public configurations values
	 */
	static public String GetPublicConfigJSON () {
		_CheckCachedValues();
		
		DataTableList dtl = new DataTableList();
		
		if ( _PublicCachedValues != null && _PublicCachedValues.size() > 0 ) {
			for (  String key : _PublicCachedValues.keySet() ) {
				dtl.AddVar(key, _PublicCachedValues.get(key));
			}
		}
		
		return dtl.ToString();
	}

	static private void _CheckCachedValues () {
		if ( _AllCachedValues == null ) {
			SetCachedValues();
		}
	}
	
	/**
	 * Refresh the cached values.
	 */
	static public void SetCachedValues () {
		_PublicCachedValues = new HashMap<String, String>();
		_AllCachedValues = new HashMap<String, String>();
		List<ConfigurationDto> configs = (new ConfigurationManager()).FindAll();
		
		if ( configs != null && configs.size() > 0 ) {
			for ( ConfigurationDto config : configs ) {
				_AllCachedValues.put( config.GetKeyId(), config.GetValue() );
				
				if ( !config.IsPrivate() ) {
					_PublicCachedValues.put( config.GetKeyId(), config.GetValue() );
				}
			}
		}
	}
	
	/**
	 * Indicate if the current environment is localhost.
	 * 
	 * @return A boolean that indicate if the current environment is localhost
	 */
	static public boolean IsDev () {
		return _IsDev;
	}
}
