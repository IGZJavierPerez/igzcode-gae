package com.igzcode.java.gae.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.igzcode.java.gae.serialization.DataTableList;

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
		DataTableList dtl = new DataTableList();
		ConfigurationManager configM = new ConfigurationManager();
		List<ConfigurationDto> configValues = new ArrayList<ConfigurationDto>();
		configValues = configM.FindAll();

		dtl.AddTable("ConfigurationValues", configValues, ConfigurationDto.class );
		
		return dtl.ToString();
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
	static public String Save (@FormParam("k") String p_key
							, @FormParam("v") String p_value
							, @FormParam("p") Boolean p_private
							) {
		
		DataTableList dtl = new DataTableList();
		
		ConfigurationDto configDto = new ConfigurationDto(p_key, p_value, p_private);
		if ( new ConfigurationManager().Save(configDto) ) {
			dtl.AddVar("OK","saved");
		}
		else {
			dtl.AddVar("KO","invalid params");
		}
		
		return dtl.ToString();
	}
	
}