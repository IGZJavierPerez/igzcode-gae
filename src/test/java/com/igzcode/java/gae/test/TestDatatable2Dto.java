package com.igzcode.java.gae.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.igzcode.java.gae.pattern.AbstractEntity;

public class TestDatatable2Dto extends AbstractEntity {
	
	@Id
	private Long _BookId;
	
	private List<String> _FileKeys = new ArrayList<String>();

	public TestDatatable2Dto () {
		super();
	}
	
	public List<String> GetFileKeys() {
		return _FileKeys;
	}

	public void SetFileKeys(List<String> _FileKeys) {
		this._FileKeys = _FileKeys;
	}
	
	public Long GetId() {
		return _BookId;
	}
}
