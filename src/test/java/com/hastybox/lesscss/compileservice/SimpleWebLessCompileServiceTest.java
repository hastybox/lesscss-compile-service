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
package com.hastybox.lesscss.compileservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;

import com.hastybox.lesscss.compileservice.compiler.LessCompiler;
import com.hastybox.lesscss.compileservice.exception.CompileException;

/**
 * @author psy
 * 
 */
public class SimpleWebLessCompileServiceTest {

	/**
	 * service to test
	 */
	private SimpleWebLessCompileService compileService;

	private LessCompiler lessCompiler;

	private String basePath;

	private ServletContext servletContext;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		compileService = new SimpleWebLessCompileService();

		lessCompiler = mock(LessCompiler.class);
		basePath = "basePath";
		servletContext = mock(ServletContext.class);

		compileService.setLessCompiler(lessCompiler);
		compileService.setBasePath(basePath);
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.SimpleWebLessCompileService#compileFromPath(java.lang.String, javax.servlet.ServletContext)}
	 * .
	 */
	@Test
	public void testCompileFromPath() {
		String requestPath = "requestPath";
		String filePath = "filePath";
		String cssCode = "cssCode";

		when(servletContext.getRealPath(basePath + requestPath)).thenReturn(
				filePath);
		when(lessCompiler.compile((File) anyObject())).thenReturn(cssCode);

		String testCode = compileService.compileFromPath(requestPath,
				servletContext);

		assertThat(testCode, equalTo(cssCode));
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.SimpleWebLessCompileService#compileFromPath(java.lang.String, javax.servlet.ServletContext)}
	 * .
	 */
	@Test
	public void testCompileFromPathNotExtracted() {
		String requestPath = "requestPath";

		when(servletContext.getRealPath(basePath + requestPath)).thenReturn(
				null);
		try {
			compileService.compileFromPath(requestPath, servletContext);

			fail();
		} catch (CompileException e) {
			// pass
		}
	}

}
