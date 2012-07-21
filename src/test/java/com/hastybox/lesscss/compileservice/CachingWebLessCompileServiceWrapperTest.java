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

/**
 * @author psy
 *
 */
public class CachingWebLessCompileServiceWrapperTest {

	private CachingWebLessCompileServiceWrapper cachingWrapper;
	
	private WebLessCompileService compileService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		cachingWrapper = new CachingWebLessCompileServiceWrapper();
		
		compileService = mock(WebLessCompileService.class);
		
		cachingWrapper.setCompileService(compileService);
		
	}

	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.CachingWebLessCompileServiceWrapper#compileFromPath(java.lang.String, javax.servlet.ServletContext)}.
	 */
	@Test
	public void testCompileFromPath() {
		ServletContext context = null;
		String path = "cssPath";
		String lessCode = "lessCode";

		when(compileService.compileFromPath(path, context)).thenReturn(lessCode);

		assertThat(cachingWrapper.cache.isEmpty(), is(true));

		String code = cachingWrapper.compileFromPath(path, context);

		assertThat(cachingWrapper.cache.size(), is(1));
		assertThat(code, is(equalTo(lessCode)));
	}

	/**
	 * Test method for {@link com.hastybox.lesscss.compileservice.CachingWebLessCompileServiceWrapper#invalidatePathCache(java.lang.String)}.
	 */
	@Test
	public void testInvalidatePathCache() {
		String path = "somePath";
		String prefix = "path:";

		cachingWrapper.cache.put(prefix + path, "someCode");

		assertThat(cachingWrapper.cache.isEmpty(), is(false));

		cachingWrapper.invalidatePathCache(path);

		assertThat(cachingWrapper.cache.isEmpty(), is(true));
	}

}
