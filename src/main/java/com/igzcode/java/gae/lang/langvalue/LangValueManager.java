package com.igzcode.java.gae.lang.langvalue;

import com.igzcode.java.util.StringUtil;

/**
 * Manage languages values.
 */
public class LangValueManager extends LangValueFactory {
	
	private LangValueManager () {}
	
	static LangValueManager langValueManager = new LangValueManager();
	
	static public LangValueManager getInstance () {
	    return langValueManager;
	}
	
	/**
	 * Save a language value into datastore.
	 * Key, value and locale must be not null.
	 * @param p_langValue The DTO to save
	 */
	public boolean saveSafe ( LangValueDto p_langValue ) {
		if (   !StringUtil.IsNullOrEmpty(p_langValue.GetKey())
			&& !StringUtil.IsNullOrEmpty(p_langValue.GetValue())
			&& !StringUtil.IsNullOrEmpty(p_langValue.GetLocale())
			) {
			
			save(p_langValue);
			
			return true;
		}
		return false;
	}
	
}
