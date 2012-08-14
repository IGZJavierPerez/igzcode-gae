package com.igzcode.java.gae.lang.langvalue;

import java.util.List;

import com.igzcode.java.util.collection.NameValueArray;
import com.igzcode.java.util.StringUtil;

/**
 * Manage languages values.
 */
public class LangValueManager extends LangValueFactory {
	
	public LangValueManager () {}
	
	/**
	 * Find all language values.
	 * @return A list with all saved language value DTOs
	 */
	public List<LangValueDto> FindAll () {
		return _FindAll();
	}
	
	/**
	 * Find language values by either locale or key. 
	 * @param p_locale Language locale (optional)
	 * @param p_key Language key (optional)
	 * @return A list of DTOs with the query result
	 */
	public List<LangValueDto> Find ( String p_locale, String p_key ) {
		NameValueArray keysAndValues = new NameValueArray();
		
		if ( !StringUtil.IsNullOrEmpty(p_key) ) {
			keysAndValues.Add( "_Key", p_key );
		}
		if ( !StringUtil.IsNullOrEmpty(p_locale) ) {
			keysAndValues.Add( "_Locale", p_locale );
		}
		
		return _FindByProperties( keysAndValues );
	}
	
	/**
	 * Find language values by locale.
	 * @param p_locale Language locale
	 * @return A list of DTOs with the query result
	 */
	public List<LangValueDto> FindByLocale ( String p_locale ) {
		return _FindByProperty("_Locale", p_locale);
	}
	
	/**
	 * Get a language value by id.
	 * @param p_id Language identifier
	 * @return A language value
	 */
	public LangValueDto Get ( long p_id ) {
		return _Get(p_id);
	}
	
	/**
	 * Save a language value into datastore.
	 * Key, value and locale must be not null.
	 * @param p_langValue The DTO to save
	 */
	public boolean Save ( LangValueDto p_langValue ) {
		if (   !StringUtil.IsNullOrEmpty(p_langValue.GetKey())
			&& !StringUtil.IsNullOrEmpty(p_langValue.GetValue())
			&& !StringUtil.IsNullOrEmpty(p_langValue.GetLocale())
			) {
			
			_Save(p_langValue);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Make a language values persistent.
	 * @param p_langValues A list with the language value DTOs
	 */
	public void Save ( List<LangValueDto> p_langValues ) {
		_Save(p_langValues);
	}
	
	/**
	 * WARNING: Make a persistent and unrecoverable delete of all entities.
	 */
	public void DeleteAll () {
    	_DeleteAll();
    }
	
	/**
	 * WARNING: Make a persistent and unrecoverable delete of entities by ids.
	 */
	public void Delete(List<Long> p_keys) {
		_DeleteByLongIds(p_keys);
	}
}
