package com.igzcode.java.gae.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.igzcode.java.gae.pattern.AbstractEntity;

public class TestDatatableDto extends AbstractEntity {
	
	@Id
	private Long _BookId;
	
	private List<Long> _FileKeys = new ArrayList<Long>();

	public TestDatatableDto () {
		super();
	}
	
	public List<Long> GetFileKeys() {
		return _FileKeys;
	}

	public void SetFileKeys(List<Long> _FileKeys) {
		this._FileKeys = _FileKeys;
	}
	
	public Long GetId() {
		return _BookId;
	}
}
