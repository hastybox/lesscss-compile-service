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
package com.hastybox.lesscss.compileservice.compiler;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.lesscss.LessException;
import org.lesscss.LessCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hastybox.lesscss.compileservice.exception.CompileException;

/**
 * A wrapper class for the LessCss Compiler
 * 
 * @author psy
 * 
 */
public class LessCssLessCompilerWrapper implements
		com.hastybox.lesscss.compileservice.compiler.LessCompiler {
	
	/**
	 * logger
	 */
	private static final Logger LOGGER;
	
	static {
		LOGGER = LoggerFactory.getLogger(LessCssLessCompilerWrapper.class);
	}

	/**
	 * use compression on CSS code
	 */
	private boolean compress;

	/**
	 * encoding to use
	 */
	private String encoding;

	/**
	 * the actual Less Compiler
	 */
	private LessCompiler compiler;

	/**
	 * @param compress
	 *            the compress to set
	 */
	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	/**
	 * @param compiler the compiler to set
	 */
	public void setCompiler(LessCompiler compiler) {
		this.compiler = compiler;
	}

	/**
	 * constructor setting default values
	 */
	public LessCssLessCompilerWrapper() {
		compiler = null;
		encoding = null;
		compress = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hastybox.lesscss.compileservice.compiler.LessCompiler#compile(java
	 * .lang.String)
	 */
	public String compile(String lessCode) {
		LessCompiler compiler = createCompiler();

		try {
			LOGGER.debug("Compiling {}", lessCode);
			
			return compiler.compile(lessCode);
		} catch (LessException e) {
			throw new CompileException("Could not compile given code", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hastybox.lesscss.compileservice.compiler.LessCompiler#compile(java
	 * .io.File)
	 */
	public String compile(File lessFile) {
		LessCompiler compiler = createCompiler();

		try {
			LOGGER.debug("Compiling LESS code from file {}", lessFile);
			
			return compiler.compile(lessFile);
		} catch (IOException e) {
			throw new CompileException("Could not load code File", e);
		} catch (LessException e) {
			throw new CompileException("Could not compile given code", e);
		}
	}

	/**
	 * builds a new LessCompiler from given configuration if there is not already one defined.
	 * 
	 * @return a new LessCompiler instance
	 */
	private synchronized LessCompiler createCompiler() {
		// in case there is already a compiler
		if (compiler == null) {
			compiler = new LessCompiler();
			compiler.setCompress(compress);
			
			if (encoding != null) {
				compiler.setEncoding(encoding);
			}
		}
		
		return compiler;

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("compress", compress)
				.append("encoding", encoding).toString();
	}

}
