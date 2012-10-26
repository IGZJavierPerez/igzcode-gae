package com.igzcode.java.gae.test;

import org.junit.Assert;
import org.junit.Test;

import com.igzcode.java.gae.configuration.ConfigurationManager;

public class ConfigurationManagerTest extends LocalDatastoreTestCase {
	
	static private ConfigurationManager _confM = ConfigurationManager.getInstance();
	
    private void _setUpTest() {}

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

