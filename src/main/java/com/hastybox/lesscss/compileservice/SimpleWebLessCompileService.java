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

import javax.servlet.ServletContext;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hastybox.lesscss.compileservice.exception.CompileException;

/**
 * Simple implementation of a WebLessCompileService
 * 
 * @author psy
 * 
 */
public class SimpleWebLessCompileService extends SimpleLessCompileService
		implements WebLessCompileService {

	/**
	 * logger
	 */
	private static final Logger LOGGER;

	static {
		LOGGER = LoggerFactory.getLogger(SimpleWebLessCompileService.class);
	}

	/**
	 * base path to less files
	 */
	protected String basePath;

	/**
	 * @param basePath
	 *            the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * constructor
	 */
	public SimpleWebLessCompileService() {
		this(null);
	}

	/**
	 * constructor
	 * 
	 * @param basePath
	 */
	public SimpleWebLessCompileService(String basePath) {
		this.basePath = basePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hastybox.lesscss.compileservice.WebLessCompileService#compileFromPath
	 * (java.lang.String, javax.servlet.ServletContext)
	 */
	public String compileFromPath(String path, ServletContext context) {
		LOGGER.debug("Compiling LESS file at {}", path);

		String lessPath = basePath + path;

		String completePath = context.getRealPath(lessPath);

		if (completePath == null) {
			String errorMsg = String
					.format("LESS file not found. Your application might not be exploded to filesystem",
							lessPath);
			LOGGER.error(errorMsg);
			throw new CompileException(errorMsg);
		}

		File lessFile = new File(completePath);

		return compileCode(lessFile);

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("lessCompiler", lessCompiler)
				.append("basePath", basePath).toString();
	}

}
