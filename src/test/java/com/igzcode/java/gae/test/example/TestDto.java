package com.igzcode.java.gae.test.example;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.annotation.Unindexed;
import com.igzcode.java.gae.pattern.AbstractEntity;
import com.igzcode.java.gae.tag.Searchable;

public class TestDto extends AbstractEntity {
	
	@Id
	private Long _BookId;
	
	private Date _Created;
	private Date _Updated;
	
	@Searchable
	private String _Title;
	private Float _Price;
	
	@Unindexed
	private Text _Summary;

	public TestDto () {
		super();
	}
	
	public TestDto (Long p_bookId, String p_title, String p_summary, Float p_price) {
		_BookId = p_bookId;
		_Title = p_title;
		_Summary = new Text(p_summary);
		_Price = p_price;
	}
	
	@PrePersist
	@SuppressWarnings("unused")
	private void _SetCreatedAndUpdated () {
		if ( _BookId == null ) {
			_Created = new Date();
		}
		_Updated = new Date();
	}
	

	public Long GetBookId() {
		return _BookId;
	}
	public void SetBookId(Long bookId) {
		this._BookId = bookId;
	}
	

	public String GetTitle() {
		return _Title;
	}
	public void SetTitle(String title) {
		this._Title = title;
	}
	

	public Float GetPrice() {
		return _Price;
	}
	public void SetPrice(Float price) {
		this._Price = price;
	}
	
	
	public Date GetCreated() {
		return _Created;
	}
	
	public Date GetUpdated() {
		return _Updated;
	}

	
	public Text GetSummary() {
		return _Summary;
	}
	public void SetSummary ( Text p_summary ) {
		this._Summary = p_summary;
	}
	
}
