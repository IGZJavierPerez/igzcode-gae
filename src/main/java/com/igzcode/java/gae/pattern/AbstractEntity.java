package com.igzcode.java.gae.pattern;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PrePersist;

import com.igzcode.java.gae.tag.Searchable;
import com.igzcode.java.util.StringUtil;

/**
 * Abstract class to implement a pseudo-full-text-search
 */
public abstract class AbstractEntity {

	private static final int MAX_TOKENS_LENGTH = 200;
	
	private Set<String> _SearchTokens = new HashSet<String>();
	
	/**
	 * A token list with the split words of its fields with annotation \@Searchables
	 * 
	 * @return The current token list of this entity
	 */
	public Set<String> GetSearchTokens () {
		return _SearchTokens;
	}
	
	@PrePersist
	
	@SuppressWarnings("unused")
	private void _SetSearchTokens () {
		
		Field[] fields = this.getClass().getDeclaredFields();
		
		StringBuffer searchableText = new StringBuffer();
		Object value;
		for ( Field field : fields ) {
			field.setAccessible(true);
			
			if ( field.isAnnotationPresent(Searchable.class) ) {
				try {
					value = field.get(this);
					if ( value != null ) {
						searchableText.append( " " + value );
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		this._SearchTokens = _GetSearchTokens( searchableText.toString() );
	}
	
	private Set<String> _GetSearchTokens ( String p_text ) {
		
		Set<String> tokens = new HashSet<String>();
		
		String[] words = StringUtil.RemoveAccents(p_text.trim()).toLowerCase().split("\\s+");
		
		String token;
		Integer pos;
		for ( String word : words ) {
			if ( tokens.size() > MAX_TOKENS_LENGTH ) {
				break;
			}
			
			for ( pos = 1; pos <= word.length(); pos++ ) {
				token = word.substring(0, pos);
				if ( !tokens.contains(token) ) {
					tokens.add(token);
				}
			}
			
		}
		
		return tokens;
	}
	
}
