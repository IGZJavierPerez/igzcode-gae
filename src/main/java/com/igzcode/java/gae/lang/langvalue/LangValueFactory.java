package com.igzcode.java.gae.lang.langvalue;

import com.googlecode.objectify.ObjectifyService;
import com.igzcode.java.gae.pattern.AbstractFactory;

/**
 * Factory to manage lang values.
 */
public class LangValueFactory extends AbstractFactory<LangValueDto> {
	
	static{
		ObjectifyService.register(LangValueDto.class);
	}
	
	public LangValueFactory() {  }
	
	
	protected LangValueDto _Get (String p_locale, String p_key) {
		return (LangValueDto) ofy().query(_DtoClass)
									.filter("_Locale", p_locale)
									.filter("_Key", p_key)
									.get();
	}

}
