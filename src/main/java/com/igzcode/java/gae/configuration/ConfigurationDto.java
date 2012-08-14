package com.igzcode.java.gae.configuration;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;

/**
 * Configuration entity
 */
@Cached
public class ConfigurationDto {
	
	@Id
	private String _KeyId;
	private String _Value;
	private Boolean _Private;
	
	public ConfigurationDto () {
		super();
	}
	
	public ConfigurationDto (String p_key, String p_value, Boolean p_private) {
		super();
		
		_KeyId = p_key;
		_Value = p_value;
		_Private = p_private;
	}

	/**
	 * Configuration identifier.
	 * @return The current configuration identifier
	 */
	public String GetKeyId() {
		return _KeyId;
	}

	/**
	 * Set the configuration identifier.
	 */
	public void SetKeyId(String _KeyId) {
		this._KeyId = _KeyId;
	}
	
	/**
	 * Configuration value.
	 * @return The current configuration value
	 */
	public String GetValue() {
		return _Value;
	}

	/**
	 * Set the configuration value.
	 */
	public void SetValue(String _Value) {
		this._Value = _Value;
	}

	/**
	 * Configuration scope.
	 * @return Indicates if a configuration is public or private
	 */
	public Boolean IsPrivate() {
		return _Private;
	}

	/**
	 * Set the configuration scope.
	 */
	public void SetPrivate(Boolean p_private) {
		this._Private = p_private;
	}
}
