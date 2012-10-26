package com.igzcode.java.gae.test;

import org.junit.Assert;
import org.junit.Test;

import com.igzcode.java.gae.configuration.ConfigurationManager;

public class ConfigurationManagerTest extends LocalDatastoreTestCase {
	
	private ConfigurationManager _confM = null;
	
    private void _setUpTest() {
    	_confM = ConfigurationManager.getInstance();
		
    }

	@Test
	public void testSetValue() {
		_setUpTest();
		
		_confM.setValue("key", "value", true);
		String keyValue = _confM.getValue("key");
		
		Assert.assertEquals("value", keyValue);
	}
	
	@Test
	public void testGetValue() {
		_setUpTest();
		
		_confM.setValue("key", "value", true);
		String keyValue = _confM.getValue("key");
		
		Assert.assertEquals("value", keyValue);
	}
	
}

