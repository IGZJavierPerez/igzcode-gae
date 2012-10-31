package com.igzcode.java.gae.configuration;


/**
 * Manage configuration values.
 */
public class ConfigurationManager extends ConfigurationFactory {
    
	/**
	 * Make a ConfigurationDto persistent.
	 * Save only if configuration key and value are not nulls.
	 * @return Indicates if the save operation was successful
	 * @throws InvalidConfigurationException 
	 */
	public void save (String key, String value) throws InvalidConfigurationException {
	    
	    if ( key == null || !key.matches("[a-zA-Z_0-9]+") ) {
            throw new InvalidConfigurationException("invalid key[" + key + "] must match [a-zA-Z_0-9]+");
        }
	    
	    if ( value == null || value.equals("") ) {
	        throw new InvalidConfigurationException("invalid value[" + value + "] must be not null or empty");
	    }
	    
	    ConfigurationDto config = new ConfigurationDto(key, value);
		super.save(config);
	}
	
	
	@SuppressWarnings("serial")
    public class InvalidConfigurationException extends Exception{
	    public InvalidConfigurationException (String msg) {
	        super(msg);
	    }
	}
	
}