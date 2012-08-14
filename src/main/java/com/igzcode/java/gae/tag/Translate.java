package com.igzcode.java.gae.tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field DTO to be automatically translated in DataTableList serialize operation.
 * The "field" property appoints to the field with the value to translate.
 * The "prefix" property is optional, it is concatenate to the field value.
 * @see DataTableList
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Translate {
	
	String field();
	
	String prefix() default "";
	
}
