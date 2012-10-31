package com.igzcode.java.gae.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.objectify.ObjectifyService;
import com.igzcode.java.gae.configuration.ConfigurationDto;
import com.igzcode.java.gae.configuration.ConfigurationManager;
import com.igzcode.java.gae.configuration.ConfigurationManager.InvalidConfigurationException;

public class ConfigurationManagerTest extends LocalDatastoreTestCase {
	
	static private ConfigurationManager _confM = new ConfigurationManager();
	
	@Override
    @Before
    public void setUp() {
	    
	    ObjectifyService.register(ConfigurationDto.class);
	    
        super.setUp();
    }



    @Test
	public void testSaveValue() throws InvalidConfigurationException {
		
		_confM.save("key", "value");
		
		ConfigurationDto config = _confM.get("key");
		
		Assert.assertEquals("value", config.getValue());
	}

	
}

