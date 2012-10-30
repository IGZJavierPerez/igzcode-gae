package com.igzcode.java.gae.pattern;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.igzcode.java.util.StringUtil;
import com.igzcode.java.util.collection.NameValue;
import com.igzcode.java.util.collection.NameValueArray;

/**
 * Abstract class to implement the DTO-Factory-Manager pattern.
 * 
 * @param <DtoType> A POJO with Objectify Java annotations
 */
public abstract class AbstractFactory<DtoType> {
    
	
	/**
	 * A Logger instance
	 */
	protected static final Logger _Log = Logger.getLogger(AbstractFactory.class.getName());
	
	/**
	 * Class that represent a POJO with Objectify Java annotations
	 */
	protected final Class<DtoType> _DtoClass;

	
//	@SuppressWarnings("unchecked")
	protected AbstractFactory (Class<DtoType> p_class) {
		
	    _DtoClass = p_class;
	    
//		_DtoClass = (Class<DtoType>) ((ParameterizedType)getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[0];
		
	}
	
	/**
	 * Gets one instance of your entity by String id.
	 * 
	 * @return If the id does not exist in the datastore returns null.
	 */
	public DtoType get ( String p_id ) {
        return ofy().load().type(_DtoClass).id(p_id).get();
	}
	
	/**
	 * Gets one instance of your entity by long id.
	 * 
	 * @return If the id does not exist in the datastore returns null.
	 */
	public DtoType get ( long p_id ) {
	    return ofy().load().type(_DtoClass).id(p_id).get();
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
	public Map<Long, DtoType> getByLongIds ( Iterable<Long> p_ids ) {
		if ( p_ids != null ) {
	        return ofy().load().type(_DtoClass).ids(p_ids);
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
	public Map<String, DtoType> getByStringIds ( Iterable<String> p_ids ) {
		if ( p_ids != null ) {
            return ofy().load().type(_DtoClass).ids(p_ids);
        }
        else {
            return null;
        }
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
	public DtoType getByProperty ( String p_filter, Object p_filterValue ) {
	    return ofy().load().type(_DtoClass).filter(p_filter, p_filterValue).first().get();
	}
	
	public DtoType getByProperties ( NameValueArray p_properties ) {
	    if ( p_properties != null && p_properties.size() > 0 ) {
	        LoadType<DtoType> loader = ofy().load().type(_DtoClass);
	        
	        for ( NameValue nameValue : p_properties ) {
	            loader.filter(nameValue.getName(), nameValue.getValue());
	        }
	        
	        return loader.first().get();
	    }
	    return null;
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
	public void save ( DtoType p_entity ) {
		ofy().save().entity(p_entity).now();
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
	public Map<Key<DtoType>, DtoType> save (Iterable<DtoType> p_entities ) {
		return ofy().save().entities( p_entities ).now();
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
	public void deleteByLongIds ( Iterable<Long> p_ids ) {
		ofy().delete().type(_DtoClass).ids(p_ids).now();
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
	public void deleteByStringIds ( Iterable<String> p_ids ) {
		ofy().delete().type(_DtoClass).ids(p_ids).now();
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
	public void delete ( String p_id ) {
		ofy().delete().type(_DtoClass).id(p_id).now();
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
	public void delete ( long p_id ) {
	    ofy().delete().type(_DtoClass).id(p_id).now();
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
	public void delete (String p_filter, Object p_filterValue) {
	    List<Key<DtoType>> keys = ofy().load().type(_DtoClass).filter(p_filter, p_filterValue).keys().list();
	    ofy().delete().keys(keys);
	}
	
	/**
	 * Delete all entities of this kind.
	 * 
	 * A quick example:
	 * 
	 * @example
	 * {@code
	 * deleteAll();
	 * }
	 */
	public void deleteAll () {
	    List<Key<DtoType>> keys = ofy().load().type(_DtoClass).keys().list();
	    ofy().delete().keys(keys).now();
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
	public List<DtoType> findAll () {
		return findAll(null);
	}
	
	/**
	 * Run a query without filters and return all ordered results.
	 * 
	 * A quick example:
	 * 
	 * {@code
	 * List<Book> books = findAll("title");
	 * }
	 * 
	 * @param p_order query order
	 * @return a list with all entities of this kind
	 */
	public List<DtoType> findAll ( String p_order ) {
		Query<DtoType> query = ofy().load().type(_DtoClass);
		
		if ( !StringUtil.isNullOrEmpty(p_order) ) {
		    query = query.order(p_order);
		}
		
		return query.list();
	}
	
	public List<DtoType> find ( String p_order, Integer p_limit, NameValueArray p_filters ) {
		
//	    limit is the maximum number of results the query will return.
//	    offset is the number of results to skip before returning any results. Results that are skipped due to offset do not count against limit.
//	    startCursor and endCursor are previously generated cursors that point to locations in a result set. If specified queries will start and end at these locations.
//	    prefetchSize is the number of results retrieved on the first call to the datastore.
	    
	    
	    // Query objects are now immutable (http://code.google.com/p/objectify-appengine/wiki/UpgradeVersion3ToVersion4)
		Query<DtoType> query = ofy().load().type(_DtoClass);
		
		if ( p_filters != null ) {
			for ( NameValue filter : p_filters ) {
			    query = query.filter( filter.getName(), filter.getValue() );
			}
		}
		
		if ( !StringUtil.isNullOrEmpty(p_order) ) {
		    query = query.order(p_order);
		}
		
		if ( p_limit != null && p_limit > 0 ) {
		    query = query.limit(p_limit);
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
	 * List<Book> books = findByProperty("price >", 10);
	 * }
	 * 
	 * @param p_filter A query filter
	 * @param p_filterValue A query filter value
	 * @param p_order A query filter value
	 * 
	 * @return a list of entities with the query results
	 */
	public List<DtoType> findByProperty ( String p_filter, Object p_filterValue, String p_order, Integer p_limit ) {
		NameValueArray filters = new NameValueArray();
		filters.add(p_filter, p_filterValue);
		return find(p_order, p_limit, filters);
	}

	public List<DtoType> findByProperty ( String p_filter, Object p_filterValue, String p_order) {
		return findByProperty(p_filter, p_filterValue, p_order, null);
	}
	
	public List<DtoType> findByProperty ( String p_filter, Object p_filterValue, Integer p_limit ) {
	    return findByProperty(p_filter, p_filterValue, null, p_limit);
	}
	
	public List<DtoType> findByProperty ( String p_filter, Object p_filterValue) {
		return findByProperty(p_filter, p_filterValue, null, null);
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
	public List<DtoType> findByProperties ( NameValueArray p_filters, String p_order, Integer p_limit ) {
	    return find(p_order, p_limit, p_filters);
	}
	
	public List<DtoType> findByProperties ( NameValueArray p_filters, String p_order ) {
	    return find(p_order, null, p_filters);
	}
	
	public List<DtoType> findByProperties ( NameValueArray p_filters, Integer p_limit ) {
	    return find(null, p_limit, p_filters);
	}
	
	public List<DtoType> findByProperties ( NameValueArray p_filters) {
	    return find(null, null, p_filters);
	}
	
}
