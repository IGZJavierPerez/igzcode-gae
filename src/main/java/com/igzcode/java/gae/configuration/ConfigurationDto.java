package com.igzcode.java.gae.configuration;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;



/**
 * Configuration entity
 */
@Entity
public class ConfigurationDto {
	
	@Id
	private String keyId;
	private String value;
	private Boolean isPrivate;
	
	public ConfigurationDto () {
		super();
	}
	
	public ConfigurationDto (String p_key, String p_value, Boolean p_private) {
		super();
		
		keyId = p_key;
		value = p_value;
		isPrivate = p_private;
	}

	/**
	 * Configuration identifier.
	 * @return The current configuration identifier
	 */
	public String getKeyId() {
		return this.keyId;
	}

	/**
	 * Set the configuration identifier.
	 */
	public void setKeyId(String p_keyId) {
		this.keyId = p_keyId;
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

	/**
	 * Configuration scope.
	 * @return Indicates if a configuration is public or private
	 */
	public Boolean isPrivate() {
		return this.isPrivate;
	}

	/**
	 * Set the configuration scope.
	 */
	public void setPrivate(Boolean p_private) {
		this.isPrivate = p_private;
	}
}
