package com.igzcode.java.gae.lang.langvalue;

import javax.persistence.Id;

/**
 * Language value entity
 */
public class LangValueDto {

	@Id
	private Long _LangValueId;
	
	private String _Key;

	private String _Locale;

	private String _Value;
	
	public LangValueDto () { }
	
	public LangValueDto ( String p_key, String p_value, String p_locale ) {
		_Key = p_key;
		_Value = p_value;
		_Locale = p_locale;
	}

	/**
	 * Language value identifier.
	 * @return The current language value identifier
	 */
	public Long GetLangValueId() {
		return _LangValueId;
	}
	
	/**
	 * Set the language value identifier.
	 */
	public void SetLangValueId(Long p_valueId) {
		this._LangValueId = p_valueId;
	}

	/**
	 * Language value key.
	 * @return The current language value key
	 */
	public String GetKey() {
		return _Key;
	}
	
	/**
	 * Set the language value key.
	 */
	public void SetKey(String p_key) {
		this._Key = p_key;
	}

	/**
	 * Configuration locale.
	 * @return The current language value locale
	 */
	public String GetLocale() {
		return _Locale;
	}
	
	/**
	 * Set the language value locale.
	 */
	public void SetLocale(String p_locale) {
		this._Locale = p_locale;
	}

	/**
	 * Configuration locale.
	 * @return The current language value text
	 */
	public String GetValue() {
		return _Value;
	}
	
	/**
	 * Set the language value text.
	 */
	public void SetValue(String p_value) {
		this._Value = p_value;
	}
	
}