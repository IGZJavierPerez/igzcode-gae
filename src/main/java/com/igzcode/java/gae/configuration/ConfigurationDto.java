package com.igzcode.java.gae.configuration;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;



/**
 * Configuration entity
 */
@Entity
public class ConfigurationDto {
	
	@Id
	private String key;
	private String value;
	
	public ConfigurationDto () {
		super();
	}
	
	public ConfigurationDto (String p_key, String p_value) {
		super();
		
		key = p_key;
		value = p_value;
	}

	/**
	 * Configuration identifier.
	 * @return The current configuration identifier
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Set the configuration identifier.
	 */
	public void setKey(String p_keyId) {
		this.key = p_keyId;
	}
	
	/**
	 * Configuration value.
	 * @return The current configuration value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Set the configuration value.
	 */
	public void setValue(String p_value) {
		this.value = p_value;
	}

}
