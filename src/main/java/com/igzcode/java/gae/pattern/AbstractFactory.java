package com.igzcode.java.gae.pattern;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;
import com.igzcode.java.util.collection.NameValue;
import com.igzcode.java.util.collection.NameValueArray;
import com.igzcode.java.util.StringUtil;

/**
 * Abstract class to implement the DTO-Factory-Manager pattern.
 * 
 * @param <DtoType> A POJO with Objectify Java annotations
 */
public abstract class AbstractFactory<DtoType> extends DAOBase {
	
	/**
	 * A Logger instance
	 */
	protected static final Logger _Log = Logger.getLogger(AbstractFactory.class.getName());
	
	/**
	 * Class that represent a POJO with Objectify Java annotations
	 */
	protected final Class<DtoType> _DtoClass;
	
	/**
	 * Identifier field name
	 */
	protected final String _KeyFieldName;
	
	@SuppressWarnings("unchecked")
	public AbstractFactory () {
		
		_DtoClass = (Class<DtoType>) ((ParameterizedType)getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
		
		Field[] fields = _DtoClass.getDeclaredFields();
		String keyFieldName = null;
		for ( Field field : fields ) {
			if ( field.isAnnotationPresent(Id.class) ) {
				keyFieldName = field.getName();
				break;
			}
		}
		
		_KeyFieldName = keyFieldName;
		
	}
	
	/**
	 * Gets one instance of your entity by String id.
	 * 
	 * @return If the id does not exist in the datastore returns null.
	 */
	protected DtoType _Get ( String p_id ) {
        return ofy().find( _DtoClass, p_id );
	}
	
	/**
	 * Gets one instance of your entity by long id.
	 * 
	 * @return If the id does not exist in the datastore returns null.
	 */
	protected DtoType _Get ( long p_id ) {
        return ofy().find( _DtoClass, p_id );
	}
	
	/**
	 * Gets multiples instances of your entity.
	 * 
	 * A quick example:
	 * 
	 * <pre>
	 * {@code
	 * List<Long> ids = new ArrayList<Long>();
	 * ids.add(14545);
	 * ids.add(14546);
	 * Map<Long, Book> books = _GetByLongIds(ids);
	 * }
	 * </pre>
	 * @return a collection of found entities mapped by its identifiers.
	 */
	protected Map<Long, DtoType> _GetByLongIds ( Iterable<Long> p_ids ) {
		if ( p_ids != null ) {
			return ofy().get(_DtoClass, p_ids);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Gets multiples instances of your entity.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<String> ids = new ArrayList<String>();
	 * ids.add("id_1");
	 * ids.add("id_2");
	 * Map<Long, Book> books = _GetByStringIds(ids);
	 * }
	 * 
	 * @return a collection of found entities mapped by its identifiers.
	 */
	protected Map<String, DtoType> _GetByStringIds ( Iterable<String> p_ids ) {
		return ofy().get(_DtoClass, p_ids);
	}
	
	/**
	 * Gets only one instance of your entity by a filter.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * Person john = _GetByProperty("email", "john.doe@email.com");
	 * }
	 * 
	 * @param p_filter a query filter
	 * @param p_filterValue a query filter value
	 * 
	 * @return if the key does not exist in the datastore returns null.
	 */
	protected DtoType _GetByProperty ( String p_filter, Object p_filterValue ) {
		Query<DtoType> query = _GetQuery();
        query.filter( p_filter, p_filterValue );
        return query.get();
	}
	
	/**
	 * Make persistent a single entity.
	 * If the entity has id, update its values.
	 * Otherwise create it and set the created id.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * Book book = new Book("title", 19.99);
	 * _Save(book);
	 * System.out.println(book.GetId()); // this will print the created id
	 * }
	 * 
	 * @param p_entity
	 */
	protected void _Save ( DtoType p_entity ) {
		ofy().put( p_entity );
	}
	
	/**
	 * Save (update or create) a collection of entities.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * ArrayList<Book> books = new ArrayList<Book>();
	 * books.add( new Book("title1", 19.99) );
	 * books.add( new Book("title2", 5.99) );
	 * _Save(books);
	 * }
	 * 
	 * @param p_entities Set of entities to save
	 * @return a map with the created or updated entities sorted by their keys
	 */
	protected Map<Key<DtoType>, DtoType> _Save (Iterable<DtoType> p_entities ) {
		return ofy().put( p_entities );
	}
	
	/**
	 * Deletes the specified entities in a parallel batch operation.
	 * This is faster and more efficient than deleting them one by one.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<Long> ids = new ArrayList<Long>();
	 * ids.add(14545);
	 * ids.add(14546);
	 * _DeleteByLongIds(ids);
	 * }
	 * 
	 * @param p_ids contain Long identifiers.
	 */	
	protected void _DeleteByLongIds ( Iterable<Long> p_ids ) {
		ArrayList<Key<DtoType>> keys = new ArrayList<Key<DtoType>>();
		for ( Long id : p_ids ) {
			keys.add( new Key<DtoType>(_DtoClass, id) );
		}
		ofy().delete( keys );
	}
	
	/**
	 * Deletes the specified entities in a parallel batch operation.
	 * This is faster and more efficient than deleting them one by one.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<String> ids = new ArrayList<String>();
	 * ids.add("14545");
	 * ids.add("14546");
	 * _DeleteByStringIds(ids);
	 * }
	 * 
	 * @param p_ids contain string identifiers.
	 */
	protected void _DeleteByStringIds ( Iterable<String> p_ids ) {
		ArrayList<Key<DtoType>> keys = new ArrayList<Key<DtoType>>();
		for ( String id : p_ids ) {
			keys.add( new Key<DtoType>(_DtoClass, id) );
		}
		ofy().delete( keys );
	}

	/**
	 * Delete the specified entity.
	 * A convenience method, shorthand for creating a key and deleting it.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * _Delete("entity_id");
	 * }
	 * 
	 * @param p_id Entity identifier
	 */
	protected void _Delete ( String p_id ) {
		ofy().delete(_DtoClass, p_id );
	}
	
	/**
	 * Delete the specified entity.
	 * A convenience method, shorthand for creating a key and deleting it.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * _Delete(14545L);
	 * }
	 * 
	 * @param p_id Entity identifier
	 */
	protected void _Delete ( long p_id ) {
		ofy().delete(_DtoClass, p_id );
	}
	
	/**
	 * Delete zero or more entities by a specified filter.
	 * A convenience method, shorthand for creating a query and set a filter.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * _Delete("price >", 4.5);
	 * }
	 * 
	 * @param p_filter A query filter
	 * @param p_filterValue A query filter value
	 */
	protected void _Delete (String p_filter, Object p_filterValue) {
		NameValueArray filterValue = new NameValueArray();
		filterValue.Add(p_filter, p_filterValue);
		_Delete(filterValue);
	}
	
	/**
	 * Delete zero or more entities by a set of specified filters.
	 * A convenience method, shorthand for creating a query and set a collection of filters.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * NameValueArray filters = new NameValueArray();
	 * filters.add("age <", 18);
	 * filters.add("gender <", Gender.Male);
	 * _Delete(filters);
	 * }
	 * 
	 * @param p_filters A query filter array
	 * @param p_filterValues A query filter value array
	 */
	protected void _Delete ( NameValueArray p_filters ) {
		Query<DtoType> query = _GetQuery();
		
		for (NameValue filter : p_filters ) {
			query.filter( filter.GetName(), filter.GetValue() );
		}
		
		Iterable<Key<DtoType>> keys = query.fetchKeys();
		
		ofy().delete(keys);
	}
	
	/**
	 * Delete all entities of this kind.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * _DeleteAll();
	 * }
	 */
	protected void _DeleteAll () {
		Query<DtoType> query = _GetQuery();
		Iterable<Key<DtoType>> keys = query.fetchKeys();
		ofy().delete(keys);
	}
	
	/**
	 * Run a query without filters and return all results.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<Book> books = _FindAll();
	 * }
	 * 
	 * @return a list with all entities of this kind
	 */
	protected List<DtoType> _FindAll () {
		return _FindAll(null);
	}
	
	/**
	 * Run a query without filters and return all ordered results.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<Book> books = _FindAll("title");
	 * }
	 * 
	 * @param p_order query order
	 * @return a list with all entities of this kind
	 */
	protected List<DtoType> _FindAll ( String p_order ) {
		Query<DtoType> query = _GetQuery();
		
		if ( !StringUtil.IsNullOrEmpty(p_order) ) {
			query.order(p_order);
		}
		
		return query.list();
	}
	
	/**
	 * "Full text search" in fields with the annotation @Searcheable
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<Book> books = _Find("cervantes", "price");
	 * }
	 * 
	 * @param p_queryString the query text to search
	 * @param p_order query order
	 * 
	 * @return a list with entities found ordered by p_order
	 */
	protected List<DtoType> _Find ( String p_queryString, String p_order ) {
		return _Find( p_queryString, p_order, null );
	}
	
	/**
	 * "Full text search" in fields with the annotation \@Searcheable
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<Book> books = _Find("cervantes", "price", 20);
	 * }
	 * 
	 * @param p_queryString the query text to search
	 * @param p_order query order
	 * @param p_limit max query results limit
	 * 
	 * @return a list with entities found ordered by p_order
	 */
	protected List<DtoType> _Find ( String p_queryString, String p_order, Integer p_limit ) {
		return _Find(p_queryString, p_order, p_limit, null);
	}
	
	protected List<DtoType> _Find ( String p_queryString, String p_order, Integer p_limit, NameValueArray p_filters ) {
		
		Query<DtoType> query = _GetQuery();
		
		if ( !StringUtil.IsNullOrEmpty(p_queryString) ) {
			String[] words = StringUtil.RemoveAccents(p_queryString.trim().toLowerCase()).split(" ");
			for ( String word : words ) {
				query.filter("_SearchTokens", word);
			}
		}
		
		if ( p_filters != null ) {
			for ( NameValue filter : p_filters ) {
				query.filter( filter.GetName(), filter.GetValue() );
			}
		}

		query.order(p_order);
		
		if ( p_limit != null && p_limit > 0 ) {
			query.limit(p_limit);
		}
				
		return query.list();
	}
	
	/**
	 * Search entities by a simple filter.
	 * A convenience method, shorthand for creating a query and set a filter.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<Book> books = _FindByProperty("price >", 10);
	 * }
	 * 
	 * @param p_filter A query filter
	 * @param p_filterValue A query filter value
	 * @param p_order A query filter value
	 * 
	 * @return a list of entities with the query results
	 */
	protected List<DtoType> _FindByProperty ( String p_filter, Object p_filterValue, String p_order, Integer p_limit ) {
		NameValueArray filters = new NameValueArray();
		filters.Add(p_filter, p_filterValue);
		return _Find(null, p_order, p_limit, filters);
	}

	protected List<DtoType> _FindByProperty ( String p_filter, Object p_filterValue, String p_order) {
		return _FindByProperty(p_filter, p_filterValue, p_order, null);
	}
	
	protected List<DtoType> _FindByProperty ( String p_filter, Object p_filterValue) {
		return _FindByProperty(p_filter, p_filterValue, null, null);
	}
	/**
	 * Search entities by a collection of filters.
	 * A convenience method, shorthand for creating a query and set a collection of filters.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * NameValueArray filters = new NameValueArray();
	 * filters.add("price >", 20);
	 * filters.add("num_pages <", 50);
	 * List<Book> books = _FindByProperties(filters);
	 * }
	 * 
	 * @param p_filtersAndValues A list with query filters
	 * 
	 * @return a list of entities with the query results
	 */
	protected List<DtoType> _FindByProperties ( NameValueArray p_filters, String p_order, Integer p_limit ) {
		return _Find(null, p_order, p_limit, p_filters);
	}
	
	protected List<DtoType> _FindByProperties ( NameValueArray p_filters, String p_order ) {
		return _FindByProperties(p_filters, p_order, null);
	}
	
	protected List<DtoType> _FindByProperties ( NameValueArray p_filters) {
		return _FindByProperties(p_filters, null, null);
	}
	
	/**
	 * A convenience method, shorthand for creating a query object.
	 */
	protected Query<DtoType> _GetQuery () {
		return ofy().query(_DtoClass);
	}
	
}
