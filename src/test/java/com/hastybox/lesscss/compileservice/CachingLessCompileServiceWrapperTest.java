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

/**
 * @author psy
 * 
 */
public class CachingLessCompileServiceWrapperTest {

	/**
	 * object to test
	 */
	private CachingLessCompileServiceWrapper cachingWrapper;

	private LessCompileService compileService;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		compileService = mock(LessCompileService.class);

		cachingWrapper = new CachingLessCompileServiceWrapper(compileService);

	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.CachingLessCompileServiceWrapper#compileCode(java.io.File)}
	 * .
	 */
	@Test
	public void testCompileCodeFile() {
		File cssFile = new File("");
		String lessCode = "lessCode";

		when(compileService.compileCode(cssFile)).thenReturn(lessCode);

		assertThat(cachingWrapper.cache.isEmpty(), is(true));

		String code = cachingWrapper.compileCode(cssFile);

		assertThat(cachingWrapper.cache.size(), is(1));
		assertThat(code, is(equalTo(lessCode)));

	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.CachingLessCompileServiceWrapper#compileCode(java.lang.String)}
	 * .
	 */
	@Test
	public void testCompileCodeString() {
		String cssCode = "cssCode";
		String lessCode = "lessCode";

		when(compileService.compileCode(cssCode)).thenReturn(lessCode);

		assertThat(cachingWrapper.cache.isEmpty(), is(true));

		String code = cachingWrapper.compileCode(cssCode);

		assertThat(cachingWrapper.cache.size(), is(1));
		assertThat(code, is(equalTo(lessCode)));
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.CachingLessCompileServiceWrapper#invalidateCache()}
	 * .
	 */
	@Test
	public void testInvalidateCache() {
		cachingWrapper.cache.put("something", "code");

		assertThat(cachingWrapper.cache.isEmpty(), is(false));

		cachingWrapper.invalidateCache();

		assertThat(cachingWrapper.cache.isEmpty(), is(true));
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.CachingLessCompileServiceWrapper#invalidateCodeCache(java.lang.String)}
	 * .
	 */
	@Test
	public void testInvalidateCodeCache() {
		String path = "somePath";
		String prefix = "code:";

		cachingWrapper.cache.put(prefix + path, "someCode");

		assertThat(cachingWrapper.cache.isEmpty(), is(false));

		cachingWrapper.invalidateCodeCache(path);

		assertThat(cachingWrapper.cache.isEmpty(), is(true));
	}

	/**
	 * Test method for
	 * {@link com.hastybox.lesscss.compileservice.CachingLessCompileServiceWrapper#invalidateFileCache(java.lang.String)}
	 * .
	 */
	@Test
	public void testInvalidateFileCache() {
		String path = "somePath";
		String prefix = "file:";

		cachingWrapper.cache.put(prefix + path, "someCode");

		assertThat(cachingWrapper.cache.isEmpty(), is(false));

		cachingWrapper.invalidateFileCache(path);

		assertThat(cachingWrapper.cache.isEmpty(), is(true));
	}

}
