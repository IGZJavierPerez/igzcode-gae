package com.igzcode.java.gae.lang.langvalue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gson.Gson;
import com.igzcode.java.gae.util.LocaleUtil;


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
@Path("/lang")
public class LangValueService {
    
    static private LangValueManager langValueManager = LangValueManager.getInstance();
    
    static private BlobstoreService _BlobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	@GET
	@Path("/{locale}")
	@Produces("application/json;charset=UTF-8")
	public String findByLocale ( @PathParam("locale") String p_locale ) throws IOException {
		HashMap<String,String> contents = LocaleUtil.getByLocale(p_locale);
		return new Gson().toJson(contents);
	}
	
	@POST
	@Path("/{locale}/create")
	@Produces("application/json;charset=UTF-8")
	public LangValueDto save (
			  @FormParam("k") String p_key
			, @FormParam("v") String p_value
			, @PathParam("locale") String p_locale
	) {
		
	    LangValueDto langValue = new LangValueDto(p_key, p_value, p_locale);
	    
		if ( langValueManager.saveSafe(langValue) ) {
		    return langValue;
		}
		
		return null;
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json;charset=UTF-8")
	public String get (
			@PathParam("id") Long p_id
	) {
		    
		LangValueDto langValue = langValueManager.get(p_id);
		if ( langValue != null ) {
		    return new Gson().toJson(langValue);
		}
		
		return null;
	}
	
	@GET
	@Path("/")
	@Produces("application/json;charset=UTF-8")
	public void FindAllCSV (@Context HttpServletRequest p_request, @Context HttpServletResponse p_response ) {
		try {
			CSVWriter writer = new CSVWriter(p_response.getWriter(), ';', '"');
			String[] values = new String[3];
			
			List<LangValueDto> langValues = LocaleUtil.findAll();
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
	@Path("/{locale}/{key}")
	@Produces("application/json;charset=UTF-8")
	public Response Find ( @PathParam("key") String p_key, @PathParam("locale") String p_locale ) {
		List<LangValueDto> langValues = langValueManager.find(p_locale, p_key);
		return Response.ok().entity( new Gson().toJson(langValues) ).build();
	}
	
	/**
     * Servlet to import a uploaded CSV file with the format: separator=; and quote_char="
     * 
     * File example:
     * "KEY1";"VALUE1"
     * "KEY2";"VALUE2"
     * ...
     */
	@POST
	@Path("/import-csv")
	@Produces("application/json;charset=UTF-8")
    public void doPost ( HttpServletRequest p_request, HttpServletResponse p_response ) throws ServletException, IOException {
        
        Map<String, List<BlobKey>> blobs = _BlobstoreService.getUploads(p_request);
        BlobKey blobKey = blobs.get("lang-file-csv").get(0);
        if ( blobKey != null ) {
            BlobstoreInputStream blobstoreInputStream = new BlobstoreInputStream(blobKey);
            InputStreamReader inputStreamReader = new InputStreamReader(blobstoreInputStream, "UTF-8");
            LocaleUtil.importContents( inputStreamReader );
        }
    }
}
