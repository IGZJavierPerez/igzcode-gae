package com.igzcode.java.gae.test;

import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.igzcode.java.gae.serialization.GsonManager;
import com.igzcode.java.gae.test.example.TestDto;
import com.igzcode.java.util.collection.NameValue;

public class GsonManagerTest extends TestCase {

	@Test
	public void testGsonManager() {

		
		// /Empty tests
		GsonManager gsonManager = new GsonManager();
		Assert.assertNull(gsonManager.GetMessages());
		GsonManager gsonManagerEmpty = new GsonManager(gsonManager.ToJson());

		Assert.assertEquals(gsonManagerEmpty.GetMessages(),
				gsonManager.GetMessages());
		Assert.assertEquals(gsonManagerEmpty.ToJson(), gsonManager.ToJson());
		Assert.assertNull(gsonManagerEmpty.Get("Dto", TestDto.class));
		Assert.assertNull(gsonManager.Get("Dto", TestDto.class));

		// /Serialize
		ArrayList<TestDto> testListDtos = new ArrayList<TestDto>();
		testListDtos.add(new TestDto(10L, "title", "summary", 5.5F));
		testListDtos.add(new TestDto(10L, "title", "summary", 5.5F));
		
		gsonManager.AddList("Dtos", testListDtos, TestDto.class);
		gsonManager.Add("String", "String");
		gsonManager.AddErrorMessage("Error1");
		gsonManager.AddMessage("Alarma", "Alarma1");
		gsonManager.AddErrorMessage("Error2");
		gsonManager.Add("Dto", new TestDto(11L, "title2", "summary", 5.5F));

		String gsonOut = gsonManager.ToJson();
		System.out.println("Add DTO Collection -> " + gsonOut);

		// /Deserialize
		GsonManager gsonArrayIn = new GsonManager(gsonOut);
		TestDto[] tests = gsonArrayIn.Get("Dtos", TestDto[].class);
		System.out.println("Get DTO Collection -> " + tests);

		String str = gsonArrayIn.Get("String", String.class);
		Assert.assertEquals(str, "String");

		
		
		// /Normal Tests
		NameValue[] mensajes = gsonArrayIn.GetMessages();
		Assert.assertEquals(tests.length, 2);
		
		Assert.assertEquals(tests[0].GetPrice(),5.5F);
		Assert.assertEquals(tests[1].GetTitle(),"title");
		

		Assert.assertEquals(mensajes.length, 3);
		Assert.assertEquals(mensajes[0].GetValue(), "Error1");
		Assert.assertEquals(mensajes[2].GetValue(), "Error2");

		TestDto test = gsonArrayIn.Get("Dto", TestDto.class);
		Assert.assertEquals(test.GetTitle(), "title2");

		// Forced errors
		String strfalse = gsonArrayIn.Get("Strong", String.class);
		Assert.assertNull(strfalse);

		try {
			gsonArrayIn.Get("String", Integer.class);
		} catch (Exception e) {
			System.out.println("Allowed forced Exception -> " + e.getMessage());
			Assert.assertTrue(true);

		}
		
		gsonArrayIn.AddList("Dtos", testListDtos, TestDto.class);
		
		
		//FIN
		Assert.assertTrue(true);
	}

}
