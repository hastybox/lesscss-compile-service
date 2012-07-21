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

import org.junit.Before;
import org.junit.Test;

import com.hastybox.lesscss.compileservice.compiler.LessCompiler;

/**
 * @author psy
 * 
 */
public class SimpleLessCompileServiceTest {

	/**
	 * service to test
	 */
	private SimpleLessCompileService compileService;

	private LessCompiler lessCompiler;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		compileService = new SimpleLessCompileService();

		lessCompiler = mock(LessCompiler.class);

		compileService.setLessCompiler(lessCompiler);

	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.SimpleLessCompileService#compileCode(java.lang.String)}
	 * .
	 */
	@Test
	public void testCompileCodeString() {
		String lessCode = "Some LESS code";
		String cssCode = "Some CSS code";

		when(lessCompiler.compile(lessCode)).thenReturn(cssCode);

		String testCode = compileService.compileCode(lessCode);

		assertThat(testCode, equalTo(cssCode));

	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.SimpleLessCompileService#compileCode(java.io.File)}
	 * .
	 */
	@Test
	public void testCompileCodeFile() {
		File lessFile = new File("");
		String cssCode = "Some CSS code";

		when(lessCompiler.compile(lessFile)).thenReturn(cssCode);

		String testCode = compileService.compileCode(lessFile);

		assertThat(testCode, equalTo(cssCode));
	}

}
