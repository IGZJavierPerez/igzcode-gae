package com.igzcode.java.gae.lang.langvalue;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.igzcode.java.gae.pattern.AbstractFactory;
import com.igzcode.java.util.StringUtil;
import com.igzcode.java.util.collection.NameValueArray;

/**
 * Factory to manage lang values.
 */
public class LangValueFactory extends AbstractFactory<LangValueDto> {
	
	static{
		ObjectifyService.register(LangValueDto.class);
	}
	
	protected LangValueFactory() {
	    super(LangValueDto.class);
	}
	
	
	public LangValueDto get (String p_locale, String p_key) {
	    NameValueArray filters = new NameValueArray();
	    filters.Add("locale", p_locale);
	    filters.Add("key", p_key);
	    
	    return getByProperties(filters);
	}
	
	/**
     * Find language values by either locale or key. 
     * @param p_locale Language locale (optional)
     * @param p_key Language key (optional)
     * @return A list of DTOs with the query result
     */
    public List<LangValueDto> find ( String p_locale, String p_key ) {
        NameValueArray filters = new NameValueArray();
        
        if ( !StringUtil.IsNullOrEmpty(p_key) ) {
            filters.Add( "key", p_key );
        }
        if ( !StringUtil.IsNullOrEmpty(p_locale) ) {
            filters.Add( "locale", p_locale );
        }
        
        return findByProperties( filters );
    }
    
    /**
     * Find language values by locale.
     * @param p_locale Language locale
     * @return A list of DTOs with the query result
     */
    public List<LangValueDto> findByLocale ( String p_locale ) {
        return findByProperty("locale", p_locale);
    }

}
