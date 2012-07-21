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

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Caching wrapper based on ConcurrentHashMap
 * 
 * @author psy
 * 
 */
public class CachingWebLessCompileServiceWrapper extends
		CachingLessCompileServiceWrapper implements WebLessCompileService {

	/**
	 * logger
	 */
	static final Logger LOGGER;

	static {
		LOGGER = LoggerFactory
				.getLogger(CachingWebLessCompileServiceWrapper.class);
	}
	
	private static String PATH_PREFIX = "path:";

	/**
	 * constructor initializing default values
	 */
	public CachingWebLessCompileServiceWrapper() {
		this(null);
	}

	/**
	 * constructor initializing default values
	 * 
	 * @param compileService
	 *            compile service to use
	 */
	public CachingWebLessCompileServiceWrapper(
			WebLessCompileService compileService) {
		cache = new ConcurrentHashMap<String, String>();
		this.compileService = compileService;
	}

	/**
	 * @param compileService
	 *            the compileService to set
	 */
	public void setCompileService(WebLessCompileService compileService) {
		this.compileService = compileService;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hastybox.lesscss.compileservice.WebLessCompileService#compileFromPath
	 * (java.lang.String, javax.servlet.ServletContext)
	 */
	public String compileFromPath(String path, ServletContext context) {

		String cacheKey = PATH_PREFIX + path;
		LOGGER.debug("Looking up {} in cache.", path);

		String cssCode = cache.get(cacheKey);

		if (cssCode == null) {
			LOGGER.debug("Did not find {} in cache. Going to compile.", path);

			cssCode = ((WebLessCompileService) getCompileService())
					.compileFromPath(path, context);

			cache.put(cacheKey, cssCode);
		}

		return cssCode;
	}
	
	/**
	 * removes entry for {@code path} from cache
	 * 
	 * @param path
	 *            key to remove from cache
	 */
	public void invalidatePathCache(String path) {
		LOGGER.debug("Invalidating {} key from cache", path);
		cache.remove(PATH_PREFIX + path);
	}

}
