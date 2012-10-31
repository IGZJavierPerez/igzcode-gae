package com.igzcode.java.gae.configuration;

import com.igzcode.java.gae.pattern.AbstractFactory;

/**
 * Factory to manage configuration values.
 */
public class ConfigurationFactory extends AbstractFactory<ConfigurationDto> {
	
	protected ConfigurationFactory () {
	    super(ConfigurationDto.class);
	}
	
}