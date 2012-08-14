package com.igzcode.java.gae.test;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public abstract class LocalDatastoreTestCase extends TestCase {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    
	@Before
    public void setUp() {
        helper.setUp();
    }
	
    @After
    public void tearDown() {
        helper.tearDown();
    }
}
