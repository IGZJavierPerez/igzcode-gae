package com.igzcode.java.gae.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.igzcode.java.gae.pattern.AbstractEntity;

public class TestDatatable4Dto extends AbstractEntity {
	
	@Id
	private Long _BookId;
	
	private List<TestDatatableDto> _FileKeys = new ArrayList<TestDatatableDto>();

	public TestDatatable4Dto () {
		super();
	}
	
	public List<TestDatatableDto> GetFileKeys() {
		return _FileKeys;
	}

	public void SetFileKeys(List<TestDatatableDto>  _FileKeys) {
		this._FileKeys = _FileKeys;
	}
	
	public Long GetId() {
		return _BookId;
	}
}
