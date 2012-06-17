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

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.hastybox.lesscss.compileservice.compiler.LessCompiler;
import com.hastybox.lesscss.compileservice.compiler.LessCssLessCompilerWrapper;

/**
 * Simple implementation of the LessCompileService interface
 * 
 * @author psy
 * 
 */
public class SimpleLessCompileService implements LessCompileService {

	/**
	 * the compiler
	 */
	protected LessCompiler lessCompiler;

	/**
	 * @param lessCompiler
	 *            the lessCompiler to set
	 */
	public void setLessCompiler(LessCompiler lessCompiler) {
		this.lessCompiler = lessCompiler;
	}

	/**
	 * constructor initializing with standard components
	 */
	public SimpleLessCompileService() {
		lessCompiler = new LessCssLessCompilerWrapper();
	}

	/**
	 * constructor
	 * 
	 * @param lessCompiler
	 */
	public SimpleLessCompileService(LessCompiler lessCompiler) {
		this.lessCompiler = lessCompiler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hastybox.lesscss.compileservice.LessCompileService#compileCode(java
	 * .lang.String)
	 */
	public String compileCode(String lessCode) {
		return lessCompiler.compile(lessCode);
	}

	/* (non-Javadoc)
	 * @see com.hastybox.lesscss.compileservice.LessCompileService#compileCode(java.io.File)
	 */
	public String compileCode(File lessFile) {
		
		return lessCompiler.compile(lessFile);
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("lessCompiler", lessCompiler)
				.toString();
	}


}
