package com.igzcode.java.gae.configuration;

import java.util.ArrayList;
import java.util.List;

import com.igzcode.java.util.StringUtil;


/**
 * Manage configuration values.
 */
public class ConfigurationManager extends ConfigurationFactory {
	
	/**
	 * If the configuration not exists, it will return null.
	 * @param p_key The configuration key
	 * @return The value of indicated configuration
	 */
	public String GetValue ( String p_key ) {
		String value = null;
		
		ConfigurationDto configDto = _Get(p_key);
		if ( configDto != null ) {
			value = configDto.GetValue();
		}
		
		return value;
	}
	
	/**
	 * Create or update a configuration value.
	 * @param p_key The configuration key
	 * @param p_value The configuration value
	 * @param p_private Indicate if a configuration must be public or not
	 */
	public void SetValue ( String p_key, String p_value, Boolean p_private ) {
		ConfigurationDto configDto = new ConfigurationDto(p_key, p_value, p_private);
		_Save(configDto);
	}
	
	/**
	 * Make persistent multiple public configuration values.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * new ConfigurationManager().SetPublicValues(new String[][]{{"key1","value1"},{"key2","value2"}})
	 * }
	 * <pre>
	 * 
	 * @param p_values Bidimensional String array with the values to save
	 */
	public void SetPublicValues ( String[][] p_values ) {
		ArrayList<ConfigurationDto> valueDtos = new ArrayList<ConfigurationDto>();
		for ( String[] value : p_values ) {
			valueDtos.add( new ConfigurationDto( value[0], value[1], false ) );
		}
		_Save(valueDtos);
	}
	
	/**
	 * Make persistent multiple private configuration values.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * new ConfigurationManager().SetPrivateValues(new String[][]{{"key1","value1"},{"key2","value2"}})
	 * }
	 * <pre>
	 * @param p_values Bidimensional String array with the values to save
	 */
	public void SetPrivateValues ( String[][] p_values ) {
		ArrayList<ConfigurationDto> valueDtos = new ArrayList<ConfigurationDto>();
		for ( String[] value : p_values ) {
			valueDtos.add( new ConfigurationDto( value[0], value[1], true ) );
		}
		_Save(valueDtos);
	}

	/**
	 * Make a persistent delete.
	 * @param p_key The configuration key
	 */
	public void Delete (String p_key) {
		_Delete(p_key);
	}

	/**
	 * Get a configuration value.
	 * @param p_key The configuration key
	 * @return A Configuration DTO
	 */
	public ConfigurationDto Get(String p_key) {
		return _Get(p_key);
	}
	
	/**
	 * Make a ConfigurationDto persistent.
	 * @param p_configurationDto The key and private values must be not null
	 * @return Indicates if the save operation was successful
	 */
	public boolean Save (ConfigurationDto p_configurationDto) {
		if ( !StringUtil.IsNullOrEmpty(p_configurationDto.GetKeyId()) && p_configurationDto.IsPrivate() != null ) {
			_Save(p_configurationDto);
			return true;
		}
		return false;
	}

	/**
	 * Find public and private configurations.
	 * @return A list with all saved configuration DTOs
	 */
	public List<ConfigurationDto> FindAll () {
		return _FindAll();
	}
	
	/**
	 * WARNING: Make a persistent and unrecoverable delete of all entities.
	 */
	public void DeleteAll() {
		_DeleteAll();
	}
	
}