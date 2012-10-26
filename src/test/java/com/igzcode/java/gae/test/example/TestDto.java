package com.igzcode.java.gae.test.example;

import java.util.Date;



import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Unindex;

@Entity
public class TestDto {
	
	@Id
	private Long bookId;
	
	@Index
	private Date created;
	
    private Date updated;
	
	@Index
	private String title;
	
	@Index
	private Float price;
	
	@Unindex
	private Text summary;
	
	@Unindex
	private String unindexedField;

	public TestDto () {
		super();
	}
	
	public TestDto (Long p_bookId, String p_title, String p_summary, Float p_price) {
		bookId = p_bookId;
		title = p_title;
		summary = new Text(p_summary);
		price = p_price;
	}
	
	@OnSave
	private void setCreatedAndUpdated () {
		if ( bookId == null ) {
			created = new Date();
		}
		updated = new Date();
	}
	

	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	
	
	public String getUnindexedField() {
        return unindexedField;
    }

    public void setUnindexedField(String unindexedField) {
        this.unindexedField = unindexedField;
    }
	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	
	
	public Date getCreated() {
		return created;
	}
	
	public Date getUpdated() {
		return updated;
	}

	
	public Text getSummary() {
		return summary;
	}
	public void setSummary ( Text p_summary ) {
		this.summary = p_summary;
	}
	
}
