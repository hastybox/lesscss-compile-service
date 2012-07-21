/* Copyright 2011-2012 The Cellador Hastybox Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hastybox.lesscss.compileservice.controller.spring;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author psy
 *
 */
public class AbstractSpringLessControllerTest {
	
	private static class TestSpringLessController extends AbstractSpringLessController {

		public ModelAndView handleRequest(HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			// not needed
			return null;
		}
		
	}
	
	private TestSpringLessController controller;
	
	private MockHttpServletRequest request;
	
	private MockHttpServletResponse response;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		controller = new TestSpringLessController();
		
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
	}

	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.controller.spring.AbstractSpringLessController#getLastModified(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testGetLastModifiedNull() {
		controller.setRelativeLastModified(null);
		
		Long value = controller.getLastModified(request);
		
		assertThat(value, is(equalTo(0L)));
	}
	
	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.controller.spring.AbstractSpringLessController#getLastModified(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testGetLastModifiedNegative() {
		controller.setRelativeLastModified(-1L);
		
		Long value = controller.getLastModified(request);
		
		assertThat(value, is(equalTo(-1L)));
	}
	
	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.controller.spring.AbstractSpringLessController#getLastModified(javax.servlet.http.HttpServletRequest)}.
	 */
	@Test
	public void testGetLastModifiedPositive() {
		controller.setRelativeLastModified(1000L);
		
		Long value = controller.getLastModified(request);
		
		assertTrue(value > 0);
	}

}
