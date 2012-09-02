package com.igzcode.java.gae.test.example;

import java.util.List;

import com.googlecode.objectify.Query;
import com.igzcode.java.util.collection.NameValueArray;

public class TestManager extends TestFactory {
	
	public TestDto Get ( long p_bookId ) {
		return _Get(p_bookId);
	}
	
	public void Save ( TestDto p_bookDto ) {
		_Save(p_bookDto);
	}

	public Query<TestDto> GetQuery(){
		return _GetQuery();
	}
	
	public void Delete ( List<Long> p_bookIds ) {
		_DeleteByLongIds( p_bookIds );
	}
	
	public void Delete ( Long p_bookId ) {
		_Delete( p_bookId );
	}
	
	public List<TestDto> Find ( String p_queryString ) {
		return _Find(p_queryString, "_Title");
	}
	
	public void DeleteAll(){
		_DeleteAll();
	}
	
	public List<TestDto> FindAll () {
		return _FindAll(null);
	}
	
	public List<TestDto> FindByProperty ( NameValueArray p_filters, String p_order, Integer p_limit ) {
		return _FindByProperties(p_filters, p_order, p_limit);
	}
	
	
	
	
}
