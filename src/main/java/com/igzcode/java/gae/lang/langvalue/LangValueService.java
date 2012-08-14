package com.igzcode.java.gae.lang.langvalue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import au.com.bytecode.opencsv.CSVWriter;

import com.igzcode.java.gae.serialization.DataTableList;
import com.igzcode.java.gae.util.LocaleUtil;
import com.igzcode.java.util.StringUtil;


/**
 * Services to manage languages values.
 * 
 * To use this services you must add the package path to the Jersey servlet configuration: 
 *
 * <pre>
 * {@code
 * <init-param>
 *   <param-name>com.sun.jersey.config.property.packages</param-name>
 *   <param-value>com.igzcode.java.gae.configuration; ...</param-value>
 * </init-param>
 * }
 * </pre>
 */
public class LangValueService {
	
	
	/**
	 * 
	 * @param p_request Must have a "locale" parameter
	 * @return A JSON with the translations
	 * @throws IOException
	 */
	@GET
	@Path("/find-by-locale")
	@Produces("application/json;charset=UTF-8")
	static public String FindByLocale (@QueryParam("l") String p_locale ) throws IOException {

		if ( !StringUtil.IsNullOrEmpty(p_locale) ) {
			DataTableList dtl = new DataTableList(p_locale);
			HashMap<String,String> contents = LocaleUtil.GetByLocale(p_locale);
			for ( String key : contents.keySet() ) {
				dtl.AddVar(key, contents.get(key));
			}
			return dtl.ToString();
		}
		else {
			return "{\"KO\":\"invalid params\"}";
		}
	}
	
	@PUT
	@Path("/save")
	@Produces("application/json;charset=UTF-8")
	static public String Save (
			  @FormParam("k") String p_key
			, @FormParam("l") String p_locale
			, @FormParam("v") String p_value
	) {
		DataTableList dtl = new DataTableList();
		
		if (   !StringUtil.IsNullOrEmpty(p_key) 
			&& !StringUtil.IsNullOrEmpty(p_locale) 
			&& !StringUtil.IsNullOrEmpty(p_value) 
			) {
			LangValueDto langValue = new LangValueDto(p_key, p_value, p_locale);
			new LangValueManager().Save(langValue);
			dtl.AddVar("OK","text saved");
		}
		else {
			dtl.AddVar("KO","invalid params");
		}
		
		return dtl.ToString();
	}
	
	@GET
	@Path("/get")
	@Produces("application/json;charset=UTF-8")
	static public String Get (
			@QueryParam("i") Long p_id
			
	) {
		DataTableList dtl = new DataTableList();
		
		if( p_id != null ){
		    
			LangValueDto langValue = (new LangValueManager()).Get(p_id);
			if ( langValue != null ) {
				dtl.AddTable("LangValue", langValue, LangValueDto.class );
			}
			else {
			    dtl.AddVar("KO", "langvalue not found");
			}
			
		}
		else {
		    dtl.AddVar("KO", "invalid params");
		}
		
		return dtl.ToString();
	}
	
	@GET
	@Path("/find-all")
	@Produces("application/json;charset=UTF-8")
	static public void FindAllCSV (@Context HttpServletRequest p_request, @Context HttpServletResponse p_response ) {
		try {
			CSVWriter writer = new CSVWriter(p_response.getWriter(), ';', '"');
			String[] values = new String[3];
			
			List<LangValueDto> langValues = LocaleUtil.FindAll();
			for ( LangValueDto langValue : langValues ) {
				values[0] = langValue.GetLocale();
				values[1] = langValue.GetKey();
				values[2] = langValue.GetValue();
				writer.writeNext(values);
			}
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("/find")
	@Produces("application/json;charset=UTF-8")
	static public String Find ( @QueryParam("k") String p_key, @QueryParam("l") String p_locale ) {
		DataTableList dtl = new DataTableList();
		
		List<LangValueDto> langValues = (new LangValueManager()).Find(p_locale, p_key);
		dtl.AddTable("LangValue", langValues, LangValueDto.class);
		return dtl.ToString();
	}
	
}
