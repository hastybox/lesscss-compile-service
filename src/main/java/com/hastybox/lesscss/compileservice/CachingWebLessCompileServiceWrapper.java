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
package com.hastybox.lesscss.compileservice;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Simple Caching wrapper based on ConcurrentHashMap
 * 
 * @author psy
 * 
 */
public class CachingWebLessCompileServiceWrapper implements
		WebLessCompileService {

	/**
	 * wrapped compile service;
	 */
	private WebLessCompileService compileService;

	/**
	 * simple cache
	 */
	private Map<String, String> cache;

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

		String cacheKey = "path:" + path;

		String cssCode = cache.get(cacheKey);

		if (cssCode == null) {
			cssCode = compileService.compileFromPath(path, context);

			cache.put(cacheKey, cssCode);
		}

		return cssCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hastybox.lesscss.compileservice.LessCompileService#compileCode(java
	 * .io.File)
	 */
	public String compileCode(File lessFile) {
		String cacheKey = "file:" + lessFile.getPath();

		String cssCode = cache.get(cacheKey);

		if (cssCode == null) {
			cssCode = compileService.compileCode(lessFile);

			cache.put(cacheKey, cssCode);
		}

		return cssCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hastybox.lesscss.compileservice.LessCompileService#compileCode(java
	 * .lang.String)
	 */
	public String compileCode(String lessCode) {
		String cacheKey = "code:" + lessCode;

		String cssCode = cache.get(cacheKey);

		if (cssCode == null) {
			cssCode = compileService.compileCode(lessCode);

			cache.put(cacheKey, cssCode);
		}

		return cssCode;
	}

	/**
	 * clears all entries from cache.
	 */
	public void invalidateCache() {
		cache.clear();
	}

	/**
	 * removes entry for {@code path} from cache
	 * 
	 * @param path
	 *            key to remove from cache
	 */
	public void invalidateCache(String path) {
		cache.remove(path);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("compileService", compileService)
				.append("cache", cache).toString();
	}

}
