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
package com.hastybox.lesscss.compileservice.compiler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

import com.hastybox.lesscss.compileservice.compiler.LessCssLessCompilerWrapper.LessCompilerCreator;
import com.hastybox.lesscss.compileservice.exception.CompileException;

/**
 * @author psy
 * 
 */
public class LessCssLessCompilerWrapperTest {

	/**
	 * test object
	 */
	private LessCssLessCompilerWrapper compilerWrapper;

	private LessCompilerCreator compilerCreator;

	private LessCompiler compiler;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		compilerWrapper = new LessCssLessCompilerWrapper();

		compilerCreator = mock(LessCompilerCreator.class);
		compiler = mock(LessCompiler.class);

		compilerWrapper.setLessCompilerCreator(compilerCreator);
		when(compilerCreator.createCompiler(anyBoolean(), (String) anyObject()))
				.thenReturn(compiler);
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.compiler.LessCssLessCompilerWrapper#compile(java.lang.String)}
	 * .
	 */
	@Test
	public void testCompileString() throws Exception {
		String lessCode = "lessCode";
		String cssCode = "cssCode";

		when(compiler.compile(lessCode)).thenReturn(cssCode);

		String testCode = compilerWrapper.compile(lessCode);

		assertThat(testCode, equalTo(cssCode));
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.compiler.LessCssLessCompilerWrapper#compile(java.lang.String)}
	 * .
	 */
	@Test
	public void testCompileStringExceptionTranslation() throws Exception {
		String lessCode = "lessCode";

		when(compiler.compile(lessCode)).thenThrow(
				new LessException(new Exception()));
		try {
			compilerWrapper.compile(lessCode);
			fail();
		} catch (CompileException e) {
			// pass
		}

	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.compiler.LessCssLessCompilerWrapper#compile(java.io.File)}
	 * .
	 */
	@Test
	public void testCompileFile() throws Exception {
		File lessFile = new File("");
		String cssCode = "cssCode";

		when(compiler.compile(lessFile)).thenReturn(cssCode);

		String testCode = compilerWrapper.compile(lessFile);

		assertThat(testCode, equalTo(cssCode));
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.compiler.LessCssLessCompilerWrapper#compile(java.io.File)}
	 * .
	 */
	@Test
	public void testCompileFileExceptionTranslation() throws Exception {
		File lessFile = new File("");

		when(compiler.compile(lessFile)).thenThrow(
				new LessException(new Exception()));
		try {
			compilerWrapper.compile(lessFile);
			fail();
		} catch (CompileException e) {
			// pass
		}

	}

}
