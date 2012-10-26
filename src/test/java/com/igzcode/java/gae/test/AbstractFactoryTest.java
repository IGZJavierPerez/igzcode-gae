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
    
	private String testTitle = "";
	private TestDto testDto = null;
	
	static private TestManager testManager = TestManager.getInstance();
	
	
    private void setUpTest() {
		testTitle= "A example title: ñéá_!\"";
		
		this.testDto = new TestDto();
		
		this.testDto.setPrice( new Float(50.50));
		this.testDto.setTitle( testTitle );
		this.testDto.setSummary( new Text("summary") );
    }

	@Test
	public void testSave() {
		setUpTest();
		
		testManager.save( testDto );
		Assert.assertEquals(1, testManager.findAll().size());
	}
	
	@Test
	public void testUpdate() {
		setUpTest();
		
		testManager.save( testDto );
		Assert.assertEquals(1, testManager.findAll().size());
		
		testDto.setTitle( "Nuevo Titulo" );
		testManager.save( testDto );
		
		Assert.assertEquals(1, testManager.findAll().size());
		
		TestDto foundTestDto = testManager.get(testDto.getBookId());
		Assert.assertEquals("Nuevo Titulo", foundTestDto.getTitle() );
	}
	
	@Test
	public void testGetById() {
		setUpTest();
		
		testManager.save( testDto );
		TestDto foundTestDto = testManager.get(testDto.getBookId());
		Assert.assertEquals(testDto.getBookId(), foundTestDto.getBookId());
	}
	
	@Test
	public void testFind() {
		setUpTest();
		
		testManager.save( testDto );
		testManager.save( testDto );
		testManager.save( testDto );
		testManager.save( testDto );
		
		List<TestDto> tests = testManager.findByProperty( "title", testDto.getTitle() );
		Assert.assertEquals("Find by title exact", 1, tests.size());
		
		
		List<TestDto> list = ofy().load().type(TestDto.class).filter("title", "false").list();
		Assert.assertEquals("Find whitout results", 0, list.size());
		
		
		Assert.assertEquals("Find whitout results", 0, testManager.findByProperty( "title", "false title" ).size());
	}
	
	@Test
	public void testDelete() {
		setUpTest();
		
		testManager.save( testDto );
		TestDto foundTestDto = testManager.get(testDto.getBookId());
		Assert.assertEquals(testDto.getBookId(), foundTestDto.getBookId());
		
		testManager.delete( testDto.getBookId() );
		
		TestDto foundSecondTestDto = testManager.get(testDto.getBookId());
		Assert.assertNull(foundSecondTestDto);
	}
	
	@Test
	public void testDeleteArray() {
		setUpTest();
		
		testManager.save( testDto );
		
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
	public void testdeleteAll() {
		setUpTest();
		
		testManager.save( testDto );
		
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
	public void testfindAll() {
		setUpTest();
		
		testManager.save( testDto );
		
		TestDto secondTestDto = new TestDto();
		secondTestDto.setPrice( new Float(50.50));
		secondTestDto.setTitle( testTitle );
		secondTestDto.setSummary( new Text("summary") );
		
		testManager.save( secondTestDto );
		
		Assert.assertEquals(2, testManager.findAll().size());
		
		List<TestDto> tests = testManager.findAll();
		Assert.assertEquals(2, tests.size());
		
	}
	

	
	@Test
	public void testFindByProperty() {
		setUpTest();
		
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
		testManager.save( test3Dto );
		
		NameValueArray filters = new NameValueArray();
		filters.Add("price", new Float(58.50));
		List<TestDto> testL = testManager.findByProperties( filters );
		Assert.assertEquals(1, testL.size());
		Assert.assertEquals("Test Expensive", testL.get(0).getTitle());
		
		filters = new NameValueArray();
		filters.Add("price", new Float(50.50));
		testL = testManager.findByProperties( filters, "price", null );
		Assert.assertEquals(2, testL.size());
		Assert.assertEquals("Test Cheaper A", testL.get(0).getTitle());
		Assert.assertEquals("Test Cheaper B", testL.get(1).getTitle());
		
		filters = new NameValueArray();
		filters.Add("price", new Float(50.50));
		testL = testManager.findByProperties( filters, "-title", 1 );
		Assert.assertEquals(1, testL.size());
		Assert.assertEquals("Test Cheaper B", testL.get(0).getTitle());
		
		filters = new NameValueArray();
		filters.Add("title", "Test Cheaper A");
		testL = testManager.findByProperties( filters, "title", null );
		Assert.assertEquals(1, testL.size());
		Assert.assertEquals("Test Cheaper A", testL.get(0).getTitle());
		
	}
}