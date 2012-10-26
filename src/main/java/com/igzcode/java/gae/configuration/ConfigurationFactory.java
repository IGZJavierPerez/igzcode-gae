package com.igzcode.java.gae.configuration;

import com.googlecode.objectify.ObjectifyService;
import com.igzcode.java.gae.pattern.AbstractFactory;

/**
 * Factory to manage configuration values.
 */
public class ConfigurationFactory extends AbstractFactory<ConfigurationDto> {
	
	static {
		ObjectifyService.register(ConfigurationDto.class);
	}
	
	protected ConfigurationFactory () {
	    super(ConfigurationDto.class);
	}
	
}