package com.igzcode.java.gae.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.igzcode.java.gae.lang.langvalue.LangValueDto;
import com.igzcode.java.gae.lang.langvalue.LangValueManager;

/**
 * Utility to manage translations using the datastore.
 */
public class LocaleUtil {

    static private final Logger logger = Logger.getLogger(LocaleUtil.class.getName());
	
	static private final String LANG_URL;
	
	static {
		LANG_URL = ConfigUtil.getInstance().getValue("LANG_URL");
	}
	
	static private HashMap< String, HashMap<String,String> > contents = null;
	
	static private List<LangValueDto> langValueDtos;
	
	static private LangValueManager langValueManager;
	
	/**
	 * Load previous saved translations.
	 */
	static public void loadContentFromDatastore () {
		langValueDtos = langValueManager.findAll();
		
		contents = new HashMap< String, HashMap<String,String> >();
		
		LangValueDto valueDto;
		int f, F= langValueDtos.size();
		for ( f=0; f<F; f++ ) {
			valueDto = langValueDtos.get(f);
			addContent(valueDto.GetLocale(), valueDto.GetKey(), valueDto.GetValue());
		}
	}
	
	static private void checkContents () {
		if ( ConfigUtil.getInstance().isDev() && contents == null ) {
			loadContentToDev();
		}
		else if ( contents == null ) {
			loadContentFromDatastore();
		}
	}
	
	static private void loadContentToDev () {
	    CSVReader reader;
	    
	    try {
	        
	        URL url = new URL( LANG_URL );
	        BufferedReader bufReader = new BufferedReader( new InputStreamReader(url.openStream(), "UTF-8") );
	        
	        reader = new CSVReader(bufReader, ';', '"');
	        
	        contents = new HashMap< String, HashMap<String,String> >();
	        
	        String [] nextLine;
	        while ((nextLine = reader.readNext()) != null) {
	            addContent(nextLine[0], nextLine[1], nextLine[2]);
	        }
	        reader.close();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.severe("Error loading lang from [" + LANG_URL + "]: " + e.getMessage());
	    }
	}
	
	/**
	 * Import translations from a CSV Reader.
	 * This deletes old translations and creates the new values from scratch.
	 * @param p_reader A input reader
	 * @throws IOException
	 */
	static public void importContents ( Reader p_reader ) throws IOException {
		
		CSVReader reader = new CSVReader(p_reader, ';', '"');
		
		contents = new HashMap< String, HashMap<String,String> >();
		
		String [] line;
		ArrayList<LangValueDto> langValues = new ArrayList<LangValueDto>();
	    while ((line = reader.readNext()) != null) {
	    	langValues.add( new LangValueDto(line[1], line[2], line[0]) );
	    	addContent(line[0], line[1], line[2]);
	    }
	    
	    reader.close();
	    
	    langValueManager.deleteAll();
	    langValueManager.save(langValues);
	}
	
	static private void addContent (String p_locale, String p_key, String p_value) {
		if ( !contents.containsKey(p_locale) ) {
			contents.put(p_locale, new HashMap<String, String>());
		}
		contents.get(p_locale).put(p_key, p_value);
	}
	
	/**
	 * Get a translated text by locale.
	 * If the translation not exists, return ???p_key???.
	 * @param p_key Translation key
	 * @param p_locale Locale code like "es_ES"
	 * @return Translation for locale specificated
	 */
	static public String getText ( String p_key, String p_locale ) {
		checkContents();
		
		String text = "???" + p_key + "???";
		if ( contents!=null && contents.containsKey(p_locale) && contents.get(p_locale).containsKey(p_key) ) {
			text = contents.get(p_locale).get(p_key);
		}
		return text;
	}
	
	/**
	 * Get all translations.
	 * @return A list with all translations
	 */
	static public List<LangValueDto> findAll () {
		checkContents();
		
		return langValueDtos;
	}
	
	/**
	 * Get all translated text for a specified locale.
	 * @param p_locale Locale code used to find translations
	 * @return A map with the translations mapped by translation key for a locale
	 */
	static public HashMap<String,String> getByLocale ( String p_locale ) {
		checkContents();
		
		return (contents!=null &&contents.containsKey(p_locale)) ? contents.get(p_locale) : new HashMap<String, String>();
	}
}
