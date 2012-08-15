package com.igzcode.java.gae.serialization;


import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.igzcode.java.gae.tag.Translate;
import com.igzcode.java.gae.util.LocaleUtil;
import com.igzcode.java.util.StringUtil;

/**
 * Serialize data as DataTableList format.
 * 
 * @see http://code.google.com/p/datatablelist/
 * @see http://js-dtl.appspot.com/
 */
public class DataTableList {


	private final String _COLUMNS_FIELD_NAME 	= "cols";

	private StringBuffer _Vars;
	private StringBuffer _Columns;
	private StringBuffer _TableRows;

	protected String _Locale = null;
	
	public DataTableList() {
		_Columns  = new StringBuffer();
		_Vars	  = new StringBuffer();
		_TableRows   = new StringBuffer();
	}
	
	public DataTableList( String p_locale ) {
		this();
		
		_Locale = p_locale;
	}
	
	/**
	 * Add a int variable.
	 * 
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddVar("name", 10);
	 * // Result: {"name":10}
	 * }
	 * </pre>
	 * @param p_label Variable name
	 * @param p_int Variable value
	 */
	public void AddVar ( String p_label, int p_int ) {
		_AddVar( p_label, String.valueOf(p_int) );
	}

	/**
	 * Add a float variable.
	 * 
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddVar("name", 10.01);
	 * // Result: {"name":10.01}
	 * }
	 * </pre>
	 * @param p_label Variable name
	 * @param p_int Variable value
	 */
	public void AddVar ( String p_label, float p_float ) {
		_AddVar( p_label, String.valueOf(p_float) );
	}

	/**
	 * Add a double variable.
	 * 
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddVar("name", 10.0198273);
	 * // Result: {"name":10.0198273}
	 * }
	 * </pre>
	 * @param p_label Variable name
	 * @param p_int Variable value
	 */
	public void AddVar ( String p_label, double p_double ) {
		_AddVar( p_label, String.valueOf(p_double) );
	}
	
	/**
	 * Add a boolean variable.
	 * 
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddVar("name", true);
	 * // Result: {"name":true}
	 * }
	 * </pre>
	 * @param p_label Variable name
	 * @param p_bool Variable value
	 */
	public void AddVar ( String p_label, boolean p_bool ) {
		_AddVar( p_label, ( p_bool ? "true" : "false" ) );
	}
	
	/**
	 * Add a String variable.
	 * 
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddVar("name", "value");
	 * // Result: {"name":"value"}
	 * }
	 * </pre>
	 * @param p_label Variable name
	 * @param p_value Variable value
	 */
	public void AddVar ( String p_label, String p_value ) {
		_AddVar( p_label, "\"" + _ParseToJson(p_value) + "\"" );
	}
	
	private void _AddVar( String p_label, String p_value ) {
		_Vars.append( ",\"" + p_label + "\":" + p_value );
	}
	
	/**
	 * Add a enum table.
	 * The table name will be the enum name to upper case.
	 * The table columns will be ["Label", "Value"].
	 * The label values are translated automatically.
	 * 
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * 
	 * Enum Gender { MALE, FEMALE }
	 * 
	 * // Locale=es_ES
	 * // GENDER.MALE=Hombre
	 * // GENDER.FEMALE=Mujer
	 * 
	 * DataTableList dtl = new DataTableList("es_ES");
	 * dtl.AddTable(Gender.values());
	 * // Result: { "cols":{"MYENUM": ["Label", "Value"]}, "MYENUM":[["Hombre", 0],["Mujer", 1]] }
	 * }
	 * </pre>
	 * @param p_values Use the static method Enum.values()
	 */
	@SuppressWarnings("rawtypes")
	public void AddTable ( Enum[] p_values ) {
		
		final String TABLE_NAME = p_values[0].getClass().getSimpleName().toUpperCase();
		final String KEY_PREFIX = TABLE_NAME+".";
		
		_AddColumns(TABLE_NAME, "[\"Label\",\"Value\"]");
		
		
		String key, label;
		String rows = "";
		for ( Object value : p_values ) {
			key = KEY_PREFIX + value.toString();
			label =  LocaleUtil.GetText(key, _Locale);
			rows += ",[\""+ label +"\",\""+ value.toString() + "\"]";
		}
		
		_TableRows.append(
				  ( _TableRows.length()>0 ? "," : "" )
				+ "\"" + TABLE_NAME + "\" : [" + rows.substring(1) + "]" 
		);
	}
	
	/**
	 * Create a new table with a DTO values.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddTable("Book", bookDto, BookDto.class);
	 * // Result: { "cols":{"Book": ["BookId", "Title"]}, "Book":[[578, "Example title"]] }
	 * }
	 * </pre>
	 * @param p_tableName Table name
	 * @param p_dto A entity instance
	 * @param p_dataClass Entity class
	 */
	public <T> void AddTable ( String p_tableName, T p_dto, Class<T> p_dataClass ) {
		AddTable(p_tableName, p_dto, null, p_dataClass);
	}
	
	/**
	 * Create a new table with a list/collection of DTOs.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddTable("Book", bookList, BookDto.class);
	 * // Result: { "cols":{"Book": ["BookId", "Title"]}, "Book":[[578, "Example title"],[579, "Example title 2"]] }
	 * }
	 * </pre>
	 * @param p_tableName Table name
	 * @param p_dtos A entity list
	 * @param p_dataClass Entity class
	 */
	public <T> void AddTable ( String p_tableName, Iterable<T> p_dtos, Class<T> p_dataClass ) {
		AddTable(p_tableName, p_dtos, null, p_dataClass);
	}
	
	/**
	 * Create a new table with a primitive array of DTOs.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddTable("Book", bookArray, BookDto.class);
	 * // Result: { "cols":{"Book": ["BookId", "Title"]}, "Book":[[578, "Example title"],[579, "Example title 2"]] }
	 * }
	 * </pre>
	 * @param p_tableName Table name
	 * @param p_dtos A entity list
	 * @param p_dataClass Entity class
	 */
	public <T> void AddTable ( String p_tableName, T[] p_dtos, Class<T> p_dataClass ) {
		AddTable(p_tableName, p_dtos, null, p_dataClass);
	}
	
	
	/**
	 * Create a new table with a DTO values of specified fields.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * String[] fields = new String[] {"_Title"};
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddTable("Book", book, fields, BookDto.class);
	 * // Result: { "cols":{"Book": ["Title"]}, "Book":[["Example title"]] }
	 * }
	 * </pre>
	 * @param p_tableName Table name
	 * @param p_dto A entity list
	 * @param p_fields Fields to be serialized
	 * @param p_dataClass Entity class
	 */
	public <T> void AddTable ( String p_tableName, T p_dto, String[] p_fields, Class<?> p_dataClass ) {
		_AddTable( p_tableName, p_dto, p_fields, false, p_dataClass );
	}
	
	/**
	 * Create a new table with a list/collection of DTOs for the specified fields.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * String[] fields = new String[] {"_Title"};
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddTable("Book", bookList, fields, BookDto.class);
	 * // Result: { "cols":{"Book": ["Title"]}, "Book":[["Example title"],["Example title 2"]] }
	 * }
	 * </pre>
	 * @param p_tableName Table name
	 * @param p_dto A entity list
	 * @param p_fields Fields to be serialized
	 * @param p_dataClass Entity class
	 */
	public <T> void AddTable ( String p_tableName, Iterable<T> p_dtos, String[] p_fields, Class<?> p_dataClass ) {
		_AddTable( p_tableName, p_dtos, p_fields, false, p_dataClass );
	}
	
	/**
	 * Create a new table with a primitive array of DTOs for the specified fields.
	 * 
	 * A quick example:
	 * <pre>
	 * {@code
	 * String[] fields = new String[] {"_Title"};
	 * DataTableList dtl = new DataTableList();
	 * dtl.AddTable("Book", bookArray, fields, BookDto.class);
	 * // Result: { "cols":{"Book": ["Title"]}, "Book":[["Example title"],["Example title 2"]] }
	 * }
	 * </pre>
	 * @param p_tableName Table name
	 * @param p_dto A entity list
	 * @param p_fields Fields to be serialized
	 * @param p_dataClass Entity class
	 */
	public <T> void AddTable ( String p_tableName, T[] p_dtos, String[] p_fields, Class<?> p_dataClass ) {
		_AddTable( p_tableName, p_dtos, p_fields, false, p_dataClass );
	}

//	private void _AddNestedTable ( String p_fieldName, Object p_values, ArrayList<String> p_fields ) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
//		_TableRows.append( "{" );
//		_AddTable(p_fieldName, p_values, p_fields, true);
//		_TableRows.append( "}," );
//	}
	
	private void _AddTable ( String p_tableName, Object p_data, String[] p_fieldList, Boolean p_nestedTable, Class<?> p_dataClass ) {
		try {
		    // Add columns
		    ArrayList<String> displayedFieldNames = _GetDisplayedFieldNames(p_dataClass, p_fieldList);
		    
		    StringBuilder columns = new StringBuilder("[");
		    for ( String fieldName : displayedFieldNames ) {
		        columns.append( "\"" + fieldName + "\"," );
		    }
		    if ( columns.length() > 1 ) {
		        columns.deleteCharAt( columns.length()-1 );
		    }
		    _AddColumns( p_tableName, columns.toString() + "]" );
			
		    // Init table rows
		    _TableRows.append( ( !p_nestedTable && _TableRows.length() > 0 ? "," : "" ) + "\"" + p_tableName + "\":[");
		    
			if ( p_data == null ) {
			    _TableRows.append("]");
			}
			else {
				
			    // Add rows
				if ( ( p_data.getClass().isArray() ) || ( p_data.getClass() == ArrayList.class ) || ( p_data instanceof Collection) ) {
					for( Object dto : (Iterable<?>) p_data ) {
						_AddRowToTable( dto, displayedFieldNames );
					}
				}
				else {
					_AddRowToTable( p_data, displayedFieldNames );
				}
				
				// Fix last char
				final int lastIdx = _TableRows.length() - 1;
				
				if ( _TableRows.charAt(lastIdx) == ',' ) {
					_TableRows.deleteCharAt(lastIdx);
				}
				
				// Finish table rows
				_TableRows.append("]");
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> _GetDisplayedFieldNames( Class<?> p_objectClass, String[] p_fields ) {
		
		ArrayList<String> declaredFieldNames = new ArrayList<String>();
		if ( p_objectClass != null ) {
			for ( Field field : p_objectClass.getDeclaredFields() ) {
				if ( !field.getName().equals("serialVersionUID") ) {
					declaredFieldNames.add( field.getName().substring(1) ); // Remove '_' prefix
				}
			}
		}
		
		if ( p_fields == null ) {
			return declaredFieldNames; // Return all declared field names
		}
		else {
			ArrayList<String> fieldNames = new ArrayList<String>();
			for( String declaredFieldName : declaredFieldNames ) {
				for( String fieldName : p_fields ) {
					if ( fieldName.equals(declaredFieldName) ) {
						fieldNames.add(fieldName);
						break;
					}
				}
			}
			return fieldNames;
		}
		
	}
	
	private void _AddColumns (String p_tableName, String p_tableColumns) {
		_Columns.append(
		     ( ( _Columns.length() > 0 ) ? "," : "" ) 
			+ "\"" + p_tableName + "\":" + p_tableColumns
		);
	}
	
	private void _AddRowToTable ( Object p_object, ArrayList<String> p_displayedFields ) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, JSONException {
		_TableRows.append( "[" );
		
		String valueString;
		Object value;
		Type fieldType;
		
		Field transField;
		Translate trans;
		String prefix; // for lang key
		for ( Field field : p_object.getClass().getDeclaredFields() ) {
			
			field.setAccessible(true);
			
			if ( !p_displayedFields.contains(field.getName().substring(1)) ) {
				continue;
			}

			value = field.get( p_object );
			valueString = "";
			fieldType = field.getType(); 
			
			if ( value != null && (fieldType == Long.class || fieldType == Integer.class || fieldType == Float.class || fieldType == Double.class) ) {
				valueString = value.toString();
			}
			else if ( value != null && fieldType == Date.class ) {
				valueString = String.valueOf( ((Date) value).getTime() );
			}
			else if ( value != null && fieldType == Boolean.class ) {
				valueString = (Boolean) value ? "true" : "false";
			}
			else if ( value != null && value.getClass().isArray() ) {
				Object[] valueArray = (Object[]) value;
			    if ( valueArray.length > 0 ) {
			    	for ( Object valueItem : valueArray ) {
			    		if ( valueItem instanceof String ) {
			    			valueString += ",\"" + valueItem + "\"";
			    		}
			    		else {
			    			valueString += "," + valueItem;
			    		}
			    	}
			    	valueString = "[" + valueString.substring(1) + "]";
			    }
			    else {
			    	valueString = "[]";
			    }
			}
			else if ( value != null && fieldType == List.class ) {
				List<?> valueArray = (List<?>) value;
				if (valueArray.size() > 0) {
					for (Object valueItem : valueArray ) {
						if ( valueItem instanceof String ) {
							valueString += ",\"" + valueItem + "\"";
						}
						else if ( valueItem instanceof List ) {
							//List<List<E>>
							JSONArray json = new JSONArray( (List<?>) valueItem );
							valueString += "," + json;
						}
						else {
							//List<List<String>> falla pq a los elementos no les pone comillas al hacer valueItem.toString()
							valueString += "," + valueItem;
						}
					}
					valueString = "[" + valueString.substring(1) + "]";
				}
				else {
					valueString = "[]";
				}
				
				/*String stringValue = value.toString().substring(1);
				stringValue = stringValue.substring(0,stringValue.length()-1);
				Object[] valueArray = stringValue.split(",");
				
				//Object[] valueArray = Arrays.asList(value).toArray();
			    if ( valueArray.length > 0 ) {
			    	for ( Object valueItem : valueArray ) {
			    		if ( valueItem instanceof String ) {
			    			valueString += ",\"" + valueItem + "\"";
			    		}
			    		else {
			    			valueString += "," + valueItem;
			    		}
			    	}
			    	valueString = "[" + valueString.substring(1) + "]";
			    }
			    else {
			    	valueString = "[]";
			    }*/
			}
			else if ( field.isAnnotationPresent(Translate.class) ) {
				trans = field.getAnnotation(Translate.class);
				
				transField = p_object.getClass().getDeclaredField( trans.field() );
				transField.setAccessible(true);
				value = transField.get(p_object); 
				if( value != null ){
					prefix = "";
					if ( value.getClass().isEnum() ) {
						prefix = value.getClass().getSimpleName().toUpperCase()+ ".";
					}
					else if ( !StringUtil.IsNullOrEmpty(trans.prefix()) ) {
						prefix = trans.prefix()+ ".";
					}
					valueString =  "\"" + _ParseToJson(LocaleUtil.GetText(prefix + value.toString(), _Locale)) +  "\"";
				}
			}
			else if ( value != null && fieldType == Text.class ) {
				valueString = "\"" + _ParseToJson( ((Text)value).getValue() ) + "\"";
			}
			else if ( value != null && value.toString() != "" ) {
				valueString = "\"" + _ParseToJson( value.toString() ) + "\"";
			}
			else if ( value == null ) {
				valueString = "null";
			}
			
			_TableRows.append( valueString + "," );
		}
		
		_TableRows.deleteCharAt(_TableRows.length() - 1);
		_TableRows.append("]," );
	}
	
	/**
	 * Serialize added tables and variables into a String.
	 * @return A valid JSON with the added data
	 */
	public String ToString() {
		
		StringBuffer json = new StringBuffer("{");
		
		if ( _Vars.length() > 0 ) {
			json.append( _Vars.toString().substring(1) + (_Columns.length()>0 ? "," : "") );
		}
		
		if ( _Columns.length() > 0 ) {
			json.append( "\"" + _COLUMNS_FIELD_NAME + "\":{" + _Columns.toString() +"},");
			json.append( _TableRows.toString() );
		}

		json.append("}");
		
		return json.toString();
		
	}
	
	private String _ParseToJson (String p_value) {
		if ( p_value != null && p_value != "" ) {
			return p_value.replaceAll("\\\\", "\\\\\\\\") // replace \ with \\
								.replaceAll("\"", "\\\\\"")
								.replaceAll("\r", "\\\\r")
								.replaceAll("\n", "\\\\n")
								.replaceAll("\t", "\\\\t")
								;
		}
		else {
			return "";
		}
		
	}
}
