package com.igzcode.java.gae.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.junit.Assert;
import org.junit.Test;

import com.google.appengine.api.datastore.Text;
import com.igzcode.java.gae.test.example.TestDto;
import com.igzcode.java.gae.test.example.TestManager;
import com.igzcode.java.util.collection.NameValueArray;

public class AbstractFactoryTest extends LocalDatastoreTestCase {
	private String _testTitle = "";
	private TestDto _testDto = null;
	private TestManager _testM = null;
	
	
    private void _setUpTest() {
		
		_testTitle= "A example title: ñéá";
		_testDto = new TestDto();
		
		_testDto.SetPrice( new Float(50.50));
		_testDto.SetTitle( _testTitle );
		_testDto.SetSummary( new Text("Summary") );
		
		_testM = new TestManager();
    }

	@Test
	public void testSave() {
		_setUpTest();
		
		_testM.Save( _testDto );
		Assert.assertEquals(1, _testM.GetQuery().list().size());
	}
	
	@Test
	public void testUpdate() {
		_setUpTest();
		
		_testM.Save( _testDto );
		Assert.assertEquals(1, _testM.GetQuery().list().size());
		
		_testDto.SetTitle( "Nuevo Titulo" );
		_testM.Save( _testDto );
		
		Assert.assertEquals(1, _testM.GetQuery().list().size());
		
		TestDto foundTestDto = _testM.Get(_testDto.GetBookId());
		Assert.assertEquals("Nuevo Titulo", foundTestDto.GetTitle() );
	}
	
	@Test
	public void testGetById() {
		_setUpTest();
		
		_testM.Save( _testDto );
		TestDto foundTestDto = _testM.Get(_testDto.GetBookId());
		Assert.assertEquals(_testDto.GetBookId(), foundTestDto.GetBookId());
	}
	
	@Test
	public void testFind() {
		_setUpTest();
		
		_testM.Save( _testDto );
		Assert.assertEquals(1, _testM.GetQuery().list().size());
		
		List<TestDto> tests = _testM.Find( _testDto.GetTitle() );
		Assert.assertEquals(1, tests.size());
		
		List<TestDto> tests2 = _testM.Find("v");
		Assert.assertEquals(0, tests2.size());
	}
	
	@Test
	public void testDelete() {
		_setUpTest();
		
		_testM.Save( _testDto );
		TestDto foundTestDto = _testM.Get(_testDto.GetBookId());
		Assert.assertEquals(_testDto.GetBookId(), foundTestDto.GetBookId());
		
		_testM.Delete( _testDto.GetBookId() );
		
		TestDto foundSecondTestDto = _testM.Get(_testDto.GetBookId());
		Assert.assertNull(foundSecondTestDto);
	}
	
	@Test
	public void testDeleteArray() {
		_setUpTest();
		
		_testM.Save( _testDto );
		
		TestDto secondTestDto = new TestDto();
		secondTestDto.SetPrice( new Float(50.50));
		secondTestDto.SetTitle( _testTitle );
		secondTestDto.SetSummary( new Text("Summary") );
		
		_testM.Save( secondTestDto );
		
		Assert.assertEquals(2, _testM.GetQuery().list().size());
		
		List<Long> bookIds = new ArrayList<Long>();
		bookIds.add(_testDto.GetBookId());
		bookIds.add(secondTestDto.GetBookId());
		
		_testM.Delete( bookIds );
		
		Assert.assertEquals(0, _testM.GetQuery().list().size());
	}
	
	@Test
	public void testDeleteAll() {
		_setUpTest();
		
		_testM.Save( _testDto );
		
		TestDto secondTestDto = new TestDto();
		secondTestDto.SetPrice( new Float(50.50));
		secondTestDto.SetTitle( _testTitle );
		secondTestDto.SetSummary( new Text("Summary") );
		
		_testM.Save( secondTestDto );
		
		Assert.assertEquals(2, _testM.GetQuery().list().size());
		
		_testM.DeleteAll();
		Assert.assertEquals(0, _testM.GetQuery().list().size());
		
	}
	
	@Test
	public void testFindAll() {
		_setUpTest();
		
		_testM.Save( _testDto );
		
		TestDto secondTestDto = new TestDto();
		secondTestDto.SetPrice( new Float(50.50));
		secondTestDto.SetTitle( _testTitle );
		secondTestDto.SetSummary( new Text("Summary") );
		
		_testM.Save( secondTestDto );
		
		Assert.assertEquals(2, _testM.GetQuery().list().size());
		
		List<TestDto> tests = _testM.FindAll();
		Assert.assertEquals(2, tests.size());
		
	}
	

	
	@Test
	public void testFindByProperty() {
		_setUpTest();
		
		TestDto testDto = new TestDto();
		testDto.SetPrice( new Float(58.50) );
		testDto.SetTitle("Test Expensive");
		testDto.SetSummary( new Text("Summary Expensive") );
		_testM.Save( testDto );
		
		TestDto test2Dto = new TestDto();
		test2Dto.SetPrice( new Float(50.50) );
		test2Dto.SetTitle("Test Cheaper A");
		test2Dto.SetSummary( new Text("Summary Cheaper") );
		_testM.Save( test2Dto );
		
		TestDto test3Dto = new TestDto();
		test3Dto.SetPrice( new Float(50.56) );
		test3Dto.SetTitle("Test Cheaper B");
		test3Dto.SetSummary( new Text("Summary Cheaper") );
		_testM.Save( test3Dto );
		
		NameValueArray filters = new NameValueArray();
		filters.Add("_Price", new Float(58.50));
		List<TestDto> testL = _testM.FindByProperty( filters, null, null );
		Assert.assertEquals(1, testL.size());
		Assert.assertEquals("Test Expensive", testL.get(0).GetTitle());
		
		filters = new NameValueArray();
		filters.Add("_Price", new Float(50.50));
		testL = _testM.FindByProperty( filters, "_Price", null );
		Assert.assertEquals(2, testL.size());
		Assert.assertEquals("Test Cheaper A", testL.get(0).GetTitle());
		Assert.assertEquals("Test Cheaper B", testL.get(1).GetTitle());
		
		filters = new NameValueArray();
		filters.Add("_Price", new Float(50.50));
		testL = _testM.FindByProperty( filters, "-_Title", 1 );
		Assert.assertEquals(1, testL.size());
		Assert.assertEquals("Test Cheaper B", testL.get(0).GetTitle());
		
		filters = new NameValueArray();
		filters.Add("_Title", "Test Cheaper A");
		testL = _testM.FindByProperty( filters, "_Title", null );
		Assert.assertEquals(1, testL.size());
		Assert.assertEquals("Test Cheaper A", testL.get(0).GetTitle());
		
	}
}