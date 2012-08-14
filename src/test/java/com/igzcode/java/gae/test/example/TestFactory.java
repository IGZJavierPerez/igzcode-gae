package com.igzcode.java.gae.test.example;


import com.googlecode.objectify.ObjectifyService;
import com.igzcode.java.gae.pattern.AbstractFactory;

public class TestFactory extends AbstractFactory<TestDto> {
	
	static {
		ObjectifyService.register(TestDto.class);
	}
	
}
