package com.igzcode.java.gae.configuration;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

/**
 * Services to manage configuration values.
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
@Path("/config")
public class ConfigurationService {
    
    static private ConfigurationManager configManager = ConfigurationManager.getInstance();
	
	/**
	 * Get all saved configuration values.
	 * Private values are included.
	 *
	 * @return A DataTable named "ConfigurationValues"
	 */
	@GET
	@Path("/find-all")
	@Produces("application/json;charset=UTF-8")
	static public String FindAll (){
	    Gson gson = new Gson();
	    
		List<ConfigurationDto> configValues = configManager.findAll();

		return gson.toJson(configValues);
	}
	
	/**
	 * Make a configuration value persistent.
	 * The request must have the following mandatory parameter: k and p
	 *
	 * @param p_key The configuration identifier (mandatory)
	 * @param p_value The configuration value (optional)
	 * @param p_private The configuration scope (mandatory)
	 *
	 * @return {"KO":"invalid params"} or {"OK":"saved"}
	 */
	@PUT
	@Path("/save")
	@Produces("application/json;charset=UTF-8")
	static public ConfigurationDto put (  @FormParam("k") String p_key
            							, @FormParam("v") String p_value
            							, @FormParam("p") Boolean p_private
            							) {
		
		ConfigurationDto configDto = new ConfigurationDto(p_key, p_value, p_private);
		
		if ( configManager.saveSafe(configDto) ) {
		    return configDto;
		}
		
	    return null;
	}
	
}