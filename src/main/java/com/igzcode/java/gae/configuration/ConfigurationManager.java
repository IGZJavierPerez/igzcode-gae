package com.igzcode.java.gae.configuration;

import java.util.ArrayList;
import java.util.List;

import com.igzcode.java.util.StringUtil;


/**
 * Manage configuration values.
 */
public class ConfigurationManager extends ConfigurationFactory {
    
    private ConfigurationManager () {}
    
    static private ConfigurationManager configurationManager = new ConfigurationManager();
    
    static public ConfigurationManager getInstance () {
        return configurationManager;
    }
    
	
	/**
	 * If the configuration not exists, it will return null.
	 * @param p_key The configuration key
	 * @return The value of indicated configuration
	 */
	public String getValue ( String p_key ) {
		String value = null;
		
		ConfigurationDto configDto = get(p_key);
		if ( configDto != null ) {
			value = configDto.getValue();
		}
		
		return value;
	}
	
	/**
	 * Create or update a configuration value.
	 * @param p_key The configuration key
	 * @param p_value The configuration value
	 * @param p_private Indicate if a configuration must be public or not
	 */
	public void setValue ( String p_key, String p_value, Boolean p_private ) {
	    if ( !StringUtil.isNullOrEmpty(p_key) && !StringUtil.isNullOrEmpty(p_value) && p_private != null ) {
	        ConfigurationDto configDto = new ConfigurationDto(p_key, p_value, p_private);
	        save(configDto);
	    }
	    else {
	        throw new NullPointerException();
	    }
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
	public void setPublicValues ( String[][] p_values ) {
		List<ConfigurationDto> valueDtos = new ArrayList<ConfigurationDto>();
		for ( String[] value : p_values ) {
			valueDtos.add( new ConfigurationDto( value[0], value[1], false ) );
		}
		save(valueDtos);
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
	public void setPrivateValues ( String[][] p_values ) {
		ArrayList<ConfigurationDto> valueDtos = new ArrayList<ConfigurationDto>();
		for ( String[] value : p_values ) {
			valueDtos.add( new ConfigurationDto( value[0], value[1], true ) );
		}
		save(valueDtos);
	}
	
	/**
	 * Make a ConfigurationDto persistent.
	 * Save only if configuration key and scope are not nulls.
	 * @param p_configurationDto The key and private values must be not null
	 * @return Indicates if the save operation was successful
	 */
	public boolean saveSafe (ConfigurationDto p_configurationDto) {
	    
		if (    !StringUtil.isNullOrEmpty(p_configurationDto.getKeyId())
		     && p_configurationDto.isPrivate() != null 
		) {
		    
			save(p_configurationDto);
			
			return true;
		}
		
		return false;
	}
	
}