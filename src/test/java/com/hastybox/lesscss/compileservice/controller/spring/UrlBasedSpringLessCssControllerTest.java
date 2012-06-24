/* Copyright 2011-2012 The Apache Software Foundation.
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

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.hastybox.lesscss.compileservice.LessCompileService;

/**
 * @author psy
 *
 */
public class UrlBasedSpringLessCssControllerTest {
	
	private UrlBasedSpringLessCssController controller;
	
	private LessCompileService compileService;
	
	private ResourceLoader resourceLoader;
	
	private Resource resource;
	
	private MockHttpServletRequest request;
	
	private MockHttpServletResponse response;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		controller = new UrlBasedSpringLessCssController();
		
		compileService = mock(LessCompileService.class);
		
		resourceLoader = mock(ResourceLoader.class);
		resource = mock(Resource.class);
		
		controller.setCompileService(compileService);
		controller.setResourceLoader(resourceLoader);
		
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		
	}

	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.controller.spring.UrlBasedSpringLessCssController#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testHandleRequest() throws Exception {
		String basePath = "/somePath/";
		String cssFile = "theFile.css";
		String lessCode = "lessCode";
		
		controller.setRequestPath(basePath);
		
		request.setRequestURI("http://somewhere.here" + basePath + cssFile);
		
		when(compileService.compileCode((File) anyObject())).thenReturn(lessCode);
		
		when(resourceLoader.getResource(anyString())).thenReturn(resource);
		when(resource.exists()).thenReturn(true);
		when(resource.getFile()).thenReturn(new File(""));
		
		ModelAndView mav = controller.handleRequest(request, response);
		
		assertThat(mav, is(nullValue()));
		assertThat(response.getStatus(), is(equalTo(200)));
	}
	
	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.controller.spring.UrlBasedSpringLessCssController#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testHandleRequestPathNotMatching() throws Exception {
		String basePath = "/somePath/";
		String cssFile = "theFile.css";
		String lessCode = "lessCode";
		
		controller.setRequestPath(basePath);
		
		request.setRequestURI("http://somewhere.here" + "/someOtherPath/" + cssFile);
		
		when(compileService.compileCode((File) anyObject())).thenReturn(lessCode);
		
		ModelAndView mav = controller.handleRequest(request, response);
		
		assertThat(mav, is(nullValue()));
		assertThat(response.getStatus(), is(equalTo(404)));
	}
	
	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.controller.spring.UrlBasedSpringLessCssController#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testHandleRequestFileNotFound() throws Exception {
		String basePath = "/somePath/";
		String cssFile = "theFile.css";
		String lessCode = "lessCode";
		
		controller.setRequestPath(basePath);
		
		request.setRequestURI("http://somewhere.here" + basePath + cssFile);
		
		when(compileService.compileCode((File) anyObject())).thenReturn(lessCode);
		
		when(resourceLoader.getResource(anyString())).thenReturn(resource);
		when(resource.exists()).thenReturn(false);
		
		ModelAndView mav = controller.handleRequest(request, response);
		
		assertThat(mav, is(nullValue()));
		assertThat(response.getStatus(), is(equalTo(404)));
	}

}
