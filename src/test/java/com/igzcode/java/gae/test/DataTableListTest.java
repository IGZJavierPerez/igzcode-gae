package com.igzcode.java.gae.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.igzcode.java.gae.serialization.DataTableList;

public class DataTableListTest extends LocalDatastoreTestCase {
	
	 
	
	 private void _setUpTest() {
		 
	 }

	 
	
	@Test
	public void testListLong() {
		_setUpTest();
		
		TestDatatableDto testDto = new TestDatatableDto();
		 ArrayList<Long> _FileKeys = new ArrayList<Long>();
		 _FileKeys.add( new Long(123) );
		 _FileKeys.add( new Long(145) );
		 _FileKeys.add( new Long(456) );
		 
		 testDto.SetFileKeys( _FileKeys );
		
		DataTableList dtl = new DataTableList();
		dtl.AddTable("listLong", testDto, TestDatatableDto.class);

		System.out.println( dtl.ToString() );
		
		String validDtl = "{\"cols\":{\"listLong\":[\"BookId\",\"FileKeys\"]},\"listLong\":[[null,[123,145,456]]]}";
		assertEquals(validDtl, dtl.ToString());
	}
	
	@Test
	public void testListString() {
		_setUpTest();
		
		TestDatatable2Dto testDto = new TestDatatable2Dto();
		 ArrayList<String> _FileKeys = new ArrayList<String>();
		 _FileKeys.add( "a" );
		 _FileKeys.add( "b" );
		 _FileKeys.add( "c" );
		
		 testDto.SetFileKeys( _FileKeys );
		 
		DataTableList dtl = new DataTableList();
		dtl.AddTable("listString", testDto, TestDatatable2Dto.class);
		
		System.out.println( dtl.ToString() );
		
		String validDtl = "{\"cols\":{\"listString\":[\"BookId\",\"FileKeys\"]},\"listString\":[[null,[\"a\",\"b\",\"c\"]]]}";
		assertEquals(validDtl, dtl.ToString());
	}
	
	@Test
	public void testListListString() {
		_setUpTest();
		
		TestDatatable3Dto testDto = new TestDatatable3Dto();
		 ArrayList<String> _FileKeys = new ArrayList<String>();
		 _FileKeys.add( "a" );
		 _FileKeys.add( "b" );
		 _FileKeys.add( "c" );
		 
		 ArrayList<List<String>> _FileKeysFinal = new ArrayList<List<String>>();
		 _FileKeysFinal.add( _FileKeys );
		 _FileKeysFinal.add( _FileKeys );
		 _FileKeysFinal.add( _FileKeys );
		
		 testDto.SetFileKeys( _FileKeysFinal );
		 
		DataTableList dtl = new DataTableList();
		dtl.AddTable("listListString", testDto, TestDatatable3Dto.class);
		
		System.out.println( dtl.ToString() );
		
		String validDtl = "{\"cols\":{\"listListString\":[\"BookId\",\"FileKeys\"]},\"listListString\":[[null,[[\"a\",\"b\",\"c\"],[\"a\",\"b\",\"c\"],[\"a\",\"b\",\"c\"]]]]}";
		assertEquals(validDtl, dtl.ToString());
	}
	

	@Test
	public void testListDto() {
		_setUpTest();
		
		TestDatatable4Dto testDto = new TestDatatable4Dto();
		ArrayList<TestDatatableDto> arrayDto = new ArrayList<TestDatatableDto>();
		TestDatatableDto testDtoIn; 
		ArrayList<Long> _FileKeys;
		
		testDtoIn = new TestDatatableDto();
		_FileKeys = new ArrayList<Long>();
		_FileKeys.add( new Long(123) );
		_FileKeys.add( new Long(145) );
		_FileKeys.add( new Long(456) );
		testDtoIn.SetFileKeys( _FileKeys );
		arrayDto.add(testDtoIn);
		
		testDtoIn = new TestDatatableDto();
		_FileKeys = new ArrayList<Long>();
		_FileKeys.add( new Long(123) );
		_FileKeys.add( new Long(145) );
		_FileKeys.add( new Long(456) );
		testDtoIn.SetFileKeys( _FileKeys );
		arrayDto.add(testDtoIn);
		
		testDtoIn = new TestDatatableDto();
		_FileKeys = new ArrayList<Long>();
		_FileKeys.add( new Long(123) );
		_FileKeys.add( new Long(145) );
		_FileKeys.add( new Long(456) );
		testDtoIn.SetFileKeys( _FileKeys );
		arrayDto.add(testDtoIn);
		
		testDto.SetFileKeys( arrayDto );
		 
		DataTableList dtl = new DataTableList();
		dtl.AddTable("listDto", testDto, TestDatatable4Dto.class);
		
		System.out.println( dtl.ToString() );

	}

}
