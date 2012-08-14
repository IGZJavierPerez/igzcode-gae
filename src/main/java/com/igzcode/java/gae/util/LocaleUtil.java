package com.igzcode.java.gae.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.igzcode.java.gae.lang.langvalue.LangValueDto;
import com.igzcode.java.gae.lang.langvalue.LangValueManager;

/**
 * Utility to manage translations using the datastore.
 */
public class LocaleUtil {

	
	static private final String _LANG_URL;
	
	static {
		_LANG_URL = ConfigUtil.Get("LANG_URL");
	}
	
	static private HashMap< String, HashMap<String,String> > _Contents = null;
	
	static private List<LangValueDto> _LangValueDtos;
	
	/**
	 * Load previous saved translations.
	 */
	static public void LoadContentFromDatastore () {
		LangValueManager valueM = new LangValueManager();
		_LangValueDtos = valueM.FindAll();
		
		_Contents = new HashMap< String, HashMap<String,String> >();
		
		LangValueDto valueDto;
		int f, F= _LangValueDtos.size();
		for ( f=0; f<F; f++ ) {
			valueDto = _LangValueDtos.get(f);
			_AddContent(valueDto.GetLocale(), valueDto.GetKey(), valueDto.GetValue());
		}
	}
	
	static private void _CheckContents (){
		if ( ConfigUtil.IsDev() && _Contents == null ) {
			_LoadContentToDev();
		}
		else if ( _Contents == null ) {
			LoadContentFromDatastore();
		}
	}
	
	static private void _LoadContentToDev () {
		try {
			URL url = new URL( _LANG_URL );
			BufferedReader bufReader = new BufferedReader( new InputStreamReader(url.openStream(), "UTF-8") );
			
			CSVReader reader = new CSVReader(bufReader, ';', '"');
			
			_Contents = new HashMap< String, HashMap<String,String> >();
			
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null) {
		    	_AddContent(nextLine[0], nextLine[1], nextLine[2]);
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Import translations from a CSV Reader.
	 * This deletes old translations and creates the new values from scratch.
	 * @param p_reader A input reader
	 * @throws IOException
	 */
	static public void ImportContents ( Reader p_reader ) throws IOException {
		LangValueManager langValueManager = new LangValueManager();
		
		CSVReader reader = new CSVReader(p_reader, ';', '"');
		
		_Contents = new HashMap< String, HashMap<String,String> >();
		
		String [] line;
		ArrayList<LangValueDto> langValues = new ArrayList<LangValueDto>();
	    while ((line = reader.readNext()) != null) {
	    	langValues.add( new LangValueDto(line[1], line[2], line[0]) );
	    	_AddContent(line[0], line[1], line[2]);
	    }
	    
	    langValueManager.DeleteAll();
	    langValueManager.Save(langValues);
	}
	
	static private void _AddContent (String p_locale, String p_key, String p_value) {
		if ( !_Contents.containsKey(p_locale) ) {
			_Contents.put(p_locale, new HashMap<String, String>());
		}
		_Contents.get(p_locale).put(p_key, p_value);
	}
	
	/**
	 * Get a translated text by locale.
	 * If the translation not exists, return ???p_key???.
	 * @param p_key Translation key
	 * @param p_locale Locale code like "es_ES"
	 * @return Translation for locale specificated
	 */
	static public String GetText ( String p_key, String p_locale ) {
		_CheckContents();
		
		String text = "???" + p_key + "???";
		if ( _Contents!=null && _Contents.containsKey(p_locale) && _Contents.get(p_locale).containsKey(p_key) ) {
			text = _Contents.get(p_locale).get(p_key);
		}
		return text;
	}
	
	/**
	 * Get all translations.
	 * @return A list with all translations
	 */
	static public List<LangValueDto> FindAll () {
		_CheckContents();
		
		return _LangValueDtos;
	}
	
	/**
	 * Get all translated text for a specified locale.
	 * @param p_locale Locale code used to find translations
	 * @return A map with the translations mapped by translation key for a locale
	 */
	static public HashMap<String,String> GetByLocale ( String p_locale ) {
		_CheckContents();
		
		return (_Contents!=null &&_Contents.containsKey(p_locale)) ? _Contents.get(p_locale) : new HashMap<String, String>();
	}
}
