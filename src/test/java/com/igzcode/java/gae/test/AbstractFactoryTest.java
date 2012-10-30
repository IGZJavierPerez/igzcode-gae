package com.igzcode.java.gae.test;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.appengine.api.datastore.Text;
import com.igzcode.java.gae.test.example.TestDto;
import com.igzcode.java.gae.test.example.TestManager;
import com.igzcode.java.util.collection.NameValueArray;

public class AbstractFactoryTest extends LocalDatastoreTestCase {
    
	final private String testTitle = "A example title: ñéá_!\"";
	
	private TestDto testDto = null;
	
	static private TestManager testManager = TestManager.getInstance();
	
	
    private void setUpTest() {
		this.testDto = new TestDto();
		this.testDto.setPrice( new Float(50.50));
		this.testDto.setTitle( testTitle );
		this.testDto.setSummary( new Text("summary") );
		testManager.save( testDto );
    }

	@Test
	public void testSave() {
		setUpTest();
		
		// save only one
		Assert.assertEquals(1, testManager.findAll().size());
		
		// save a collection of DTOs (batch save)
		List<TestDto> dtos = new ArrayList<TestDto>();
		dtos.add(new TestDto());
		dtos.add(new TestDto());
		dtos.add(new TestDto());
		dtos.add(new TestDto());
		testManager.save( dtos );
		
		Assert.assertEquals(dtos.size() + 1, testManager.findAll().size());
	}
	
	@Test
	public void testUpdate() {
		setUpTest();
		
		Assert.assertEquals(1, testManager.findAll().size());
		
		final String NEW_TITLE = "New Title";
		
		testDto.setTitle( NEW_TITLE );
		testManager.save( testDto );
		
		Assert.assertEquals(1, testManager.findAll().size());
		
		TestDto foundTestDto = testManager.get(testDto.getBookId());
		Assert.assertEquals(NEW_TITLE, foundTestDto.getTitle() );
	}
	
	@Test
	public void testGetById() {
		setUpTest();
		
		TestDto foundTestDto = testManager.get(testDto.getBookId());
		Assert.assertEquals(testDto.getTitle(), foundTestDto.getTitle());
        Assert.assertEquals(testDto.getBookId(), foundTestDto.getBookId());
        Assert.assertEquals(testDto.getCreated(), foundTestDto.getCreated());
        Assert.assertEquals(testDto.getPrice(), foundTestDto.getPrice());
        Assert.assertEquals(testDto.getSummary(), foundTestDto.getSummary());
        Assert.assertEquals(testDto.getUpdated(), foundTestDto.getUpdated());
	}
	
	@Test
	public void testDelete() {
		setUpTest();
		
		TestDto foundTestDto = testManager.get(testDto.getBookId());
		Assert.assertEquals(testDto.getBookId(), foundTestDto.getBookId());
		
		testManager.delete( testDto.getBookId() );
		
		TestDto foundSecondTestDto = testManager.get(testDto.getBookId());
		Assert.assertNull(foundSecondTestDto);
	}
	
	@Test
	public void testDeleteArray() {
		setUpTest();
		
		TestDto secondTestDto = new TestDto();
		secondTestDto.setPrice( new Float(50.50));
		secondTestDto.setTitle( testTitle );
		secondTestDto.setSummary( new Text("summary") );
		
		testManager.save( secondTestDto );
		
		Assert.assertEquals(2, testManager.findAll().size());
		
		List<Long> bookIds = new ArrayList<Long>();
		bookIds.add(testDto.getBookId());
		bookIds.add(secondTestDto.getBookId());
		
		testManager.deleteByLongIds( bookIds );
		
		Assert.assertEquals(0, testManager.findAll().size());
	}
	
	@Test
	public void testDeleteAll() {
		setUpTest();
		
		TestDto secondTestDto = new TestDto();
		secondTestDto.setPrice( new Float(50.50));
		secondTestDto.setTitle( testTitle );
		secondTestDto.setSummary( new Text("summary") );
		
		testManager.save( secondTestDto );
		
		Assert.assertEquals(2, testManager.findAll().size());
		
		testManager.deleteAll();
		Assert.assertEquals(0, testManager.findAll().size());
		
	}
	
	@Test
	public void testFindAll() {
		setUpTest();
		
		TestDto secondTestDto = new TestDto();
		secondTestDto.setPrice( new Float(50.50));
		secondTestDto.setTitle( testTitle );
		secondTestDto.setSummary( new Text("summary") );
		
		testManager.save( secondTestDto );
		
		List<TestDto> tests = testManager.findAll();
		Assert.assertEquals(2, tests.size());
		
	}
	
	@Test
    public void testFindByProperty() {
	    
        // Create a set of DTOs
        TestDto testDto = new TestDto();
        testDto.setPrice( new Float(58.50) );
        testDto.setTitle("Unique title");
        testDto.setSummary( new Text("Summary") );
        testManager.save( testDto );
        
        TestDto test2Dto = new TestDto();
        test2Dto.setPrice( new Float(10) );
        test2Dto.setTitle("Repeat title");
        test2Dto.setSummary( new Text("Summary") );
        testManager.save( test2Dto );
        
        TestDto test3Dto = new TestDto();
        test3Dto.setPrice( new Float(20) );
        test3Dto.setTitle("Repeat title");
        test3Dto.setSummary( new Text("Summary") );
        test3Dto.setUnindexedField("value");
        testManager.save( test3Dto );
        
        // find by title exact
        List<TestDto> tests = testManager.findByProperty( "title", testDto.getTitle() );
        Assert.assertEquals("Find by title exact", 1, tests.size());
        
        // find by property with order
        tests = testManager.findByProperty( "title", "Repeat title", "price" );
        Assert.assertEquals( 2, tests.size());
        Assert.assertEquals( test2Dto.getPrice(), tests.get(0).getPrice());
        Assert.assertEquals( test3Dto.getPrice(), tests.get(1).getPrice());
        
        // find by property with limit
        tests = testManager.findByProperty( "title", "Repeat title", 1 );
        Assert.assertEquals( 1, tests.size());
        
        // find by property with order and limit
        tests = testManager.findByProperty( "title", "Repeat title", "price", 1 );
        Assert.assertEquals( 1, tests.size());
        
        
        // find without results
        List<TestDto> list = ofy().load().type(TestDto.class).filter("title", "false").list();
        Assert.assertEquals("Find whitout results", 0, list.size());
        
    }
	
	@Test
	public void testFindByProperties () {
		
		// Create a set of DTOs
		TestDto testDto = new TestDto();
		testDto.setPrice( new Float(58.50) );
		testDto.setTitle("Test Expensive");
		testDto.setSummary( new Text("Summary Expensive") );
		testManager.save( testDto );
		
		TestDto test2Dto = new TestDto();
		test2Dto.setPrice( new Float(50.50) );
		test2Dto.setTitle("Test Cheaper A");
		test2Dto.setSummary( new Text("Summary Cheaper") );
		testManager.save( test2Dto );
		
		TestDto test3Dto = new TestDto();
		test3Dto.setPrice( new Float(50.50) );
		test3Dto.setTitle("Test Cheaper B");
		test3Dto.setSummary( new Text("Summary Cheaper") );
		test3Dto.setUnindexedField("value");
		testManager.save( test3Dto );
		
		
		// find one entity by its properties
		NameValueArray filters = new NameValueArray();
		filters.add("price", testDto.getPrice());
		filters.add("created", testDto.getCreated());
		filters.add("title", testDto.getTitle());
		List<TestDto> testL = testManager.findByProperties( filters );
		
		Assert.assertEquals(1, testL.size());
		
		Assert.assertEquals(testDto.getTitle(), testL.get(0).getTitle());
		Assert.assertEquals(testDto.getBookId(), testL.get(0).getBookId());
		Assert.assertEquals(testDto.getCreated(), testL.get(0).getCreated());
		Assert.assertEquals(testDto.getPrice(), testL.get(0).getPrice());
		Assert.assertEquals(testDto.getSummary(), testL.get(0).getSummary());
		Assert.assertEquals(testDto.getUpdated(), testL.get(0).getUpdated());
		
		
		
		filters = new NameValueArray();
		filters.add("price", new Float(50.50));
		
		// find various entities by its properties
		testL = testManager.findByProperties( filters );
		Assert.assertEquals(2, testL.size());
		
		
		// find various entities by its properties with oder
		testL = testManager.findByProperties( filters, "title" );
		Assert.assertEquals(2, testL.size());
		Assert.assertEquals(test2Dto.getTitle(), testL.get(0).getTitle());
        Assert.assertEquals(test3Dto.getTitle(), testL.get(1).getTitle());
		
        
        // find various entities by its properties with oder desc
		testL = testManager.findByProperties( filters, "-title" );
		Assert.assertEquals(2, testL.size());
		Assert.assertEquals(test2Dto.getTitle(), testL.get(1).getTitle());
        Assert.assertEquals(test3Dto.getTitle(), testL.get(0).getTitle());
        
        
        // find various entities by its properties with limit
        testL = testManager.findByProperties( filters, 1 );
        Assert.assertEquals(1, testL.size());
        
        
        // find various entities by its properties with limit and order
        testL = testManager.findByProperties( filters, "-title", 1 );
        Assert.assertEquals(1, testL.size());
        Assert.assertEquals(test3Dto.getTitle(), testL.get(0).getTitle());
        
        
        // find by an unindexed field
        filters = new NameValueArray();
        filters.add("unindexedField", "value");
        testL = testManager.findByProperties( filters );
        Assert.assertEquals(0, testL.size());
        
        
        // find by a text field should not be allowed
		filters = new NameValueArray();
		filters.add("summary", "Summary Cheaper");
		testL = testManager.findByProperties( filters );
		Assert.assertEquals(0, testL.size());
		
	}
}