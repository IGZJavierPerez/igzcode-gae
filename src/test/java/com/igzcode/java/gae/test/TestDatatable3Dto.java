package com.igzcode.java.gae.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.igzcode.java.gae.pattern.AbstractEntity;

public class TestDatatable3Dto extends AbstractEntity {
	
	@Id
	private Long _BookId;
	
	private List<List<String>> _FileKeys = new ArrayList<List<String>>();

	public TestDatatable3Dto () {
		super();
	}
	
	public List<List<String>> GetFileKeys() {
		return _FileKeys;
	}

	public void SetFileKeys(List<List<String>>  _FileKeys) {
		this._FileKeys = _FileKeys;
	}
	
	public Long GetId() {
		return _BookId;
	}
}
