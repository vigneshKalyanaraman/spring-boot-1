//package com.obs.controller;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.Ignore;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import static org.junit.jupiter.api.Assertions.fail;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import com.obs.TravelProAuthApplicationTests;
//
//@ActiveProfiles("utest")
////@Sql(scripts = "classpath:testdata.sql")
//
//public class UserAPITest extends TravelProAuthApplicationTests {
//	
//	private static final Logger log = LoggerFactory.getLogger(UserAPITest.class);
//	
//	@Ignore
//	@Test
//	@Rollback
//	public void countryOk() throws Exception 
//	{
//		try
//		{
//			
//			MvcResult  mvcResult = mvc.perform(MockMvcRequestBuilders
//					.get("/country")
//					.contentType(MediaType.APPLICATION_JSON)
//					.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
//			System.out .println("testtt");
//			System.out.println(mvcResult.getResponse().getContentAsString());
//		}
//		catch(Exception e)
//		{
//			fail(e);
//			log.error(e.toString());
//		}
//	}
//	
//
//}
