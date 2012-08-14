package com.igzcode.java.gae.serialization;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.igzcode.java.util.collection.NameValue;

public class GsonManager {

	
	/**
	 * Gson escapes HTML characters such as < > etc
	 */
	private Gson _Gson = new GsonBuilder().disableHtmlEscaping().create();
	private JsonObject _JsonObject;
	private Map<String, Object> _Data = new HashMap<String, Object>();

	/**
	 * Constructor method for serialization
	 */
	public GsonManager() 
	{
		
	}

	/**
	 * Constructor method for deserialization
	 * 
	 * @param p_json
	 *            the Json String for deserialization
	 * 
	 * @throws JsonSyntaxException
	 */
	public GsonManager(String p_json) throws JsonSyntaxException {
		_JsonObject = new JsonParser().parse(p_json).getAsJsonObject();
	}

	
	/**
	 * Returns the value to which the specified key is mapped, or null if this
	 * map contains no mapping for the key. More formally, if this map contains
	 * a mapping from a key k to a value v such that (key==null ? k==null :
	 * key.equals(k)), then this method returns v; otherwise it returns null.
	 * (There can be at most one such mapping.)
	 * 
	 * A return value of null does not necessarily indicate that the map
	 * contains no mapping for the key; it's also possible that the map
	 * explicitly maps the key to null. The containsKey operation may be used to
	 * distinguish these two cases.
	 * 
	 * @param p_key
	 *            the key whose associated value is to be returned
	 * @param p_class
	 *            the class of T Returns: an object of type T from the string
	 * 
	 * @return an object of type T to which the specified key is mapped, or null
	 *         if this map contains no mapping for the key
	 */
	public <T> T Get(String p_key, Class<T> p_class) {

		if (_JsonObject == null)
			return null;

		return _Gson.fromJson(_JsonObject.get(p_key), p_class);

	}
	
	public Boolean HasKey (String p_memberName) {
		return _JsonObject.has(p_memberName);
	}
	
	public <T> T Find (String p_key, Class<T> p_class) {
		if ( HasKey(p_key) ) {
			return Get(p_key, p_class);
		}
		return null;
	}
	


	/**
	 * Associates the specified p_obj with the specified class name. If the map
	 * previously contained a mapping for the class name, the old value is
	 * replaced by the specified value.
	 * 
	 * @param p_key
	 *            key with which the specified value is to be associated value
	 * 
	 * @param p_obj
	 *            object to be associated with the specified key
	 * 
	 * @return the previous value associated with p_key, or null if there was no
	 *         mapping for p_key. (A null return can also indicate that the map
	 *         previously associated null with p_className, if the
	 *         implementation supports null values.)
	 */
	public void Add(String p_key, Object p_obj) {
		_Data.put(p_key, p_obj);
	}

	/**
	 * Associates the specified p_dtos with the specified class name. If the map
	 * previously contained a mapping for the class name, the old value is
	 * replaced by the specified value.
	 * 
	 * @param p_key
	 *            key with which the specified value is to be associated value
	 * 
	 * @param p_dtos
	 *            dto list to be associated with the specified key
	 * 
	 * @param p_class
	 *            class type of the dtos
	 * 
	 * @return the previous value associated with p_key, or null if there was no
	 *         mapping for p_key. (A null return can also indicate that the map
	 *         previously associated null with p_className, if the
	 *         implementation supports null values.)
	 */
	public <T> void AddList(String p_key, List<T> p_dtos, Class<T> p_class) {

		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(p_class, p_dtos.size());

		int cont = 0;
		for (T dto : p_dtos) {
			array[cont++] = dto;
		} 

		_Data.put(p_key, array);
	}

	/**
	 * Returns the serialization of the assigned data or {} if empty
	 * 
	 * @return a String with the Json which represents the assigned Data
	 * 
	 */
	public String ToJson() {
		return _Gson.toJson(_Data);
	}
	
	
	/**
	 * Add a NameValue with name "error" and value p_value
	 * 
	 * @param p_value
	 *            Text of the error
	 * 
	 */
	public void AddErrorMessage(String p_value) {
		String name = "error";
		AddMessage(name, p_value);
	}

	/**
	 * 
	 * Add a NameValue with name p_name and value p_value
	 * 
	 * @param p_name
	 *            Message type
	 * 
	 * @param p_value
	 *            Text of the Message
	 * 
	 */
	public void AddMessage(String p_name, String p_value) {

		NameValue[] arrayValue = (NameValue[]) _Data.get("GsonInfo");
		List<NameValue> actualValue = null;

		if (arrayValue == null) {

			actualValue = new ArrayList<NameValue>();
		} else {
			actualValue = new ArrayList<NameValue>(Arrays.asList(arrayValue));
		}

		actualValue.add(new NameValue(p_name, p_value));
		AddList("GsonInfo", actualValue, NameValue.class);

	}

	/**
	 * Returns the system messages or null
	 * 
	 * @return a NameValue[] with the pair Name - Error Text
	 * 
	 * 
	 */
	public NameValue[] GetMessages() {

		return Get("GsonInfo", NameValue[].class);

	}

	/**
	 * Remove all previous added messages.
	 */
	public void ClearMessages() {

		_Data.remove("GsonInfo");

	}



}
