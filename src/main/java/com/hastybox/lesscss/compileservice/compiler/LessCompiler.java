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

/**
 * Interface giving access to a Less Compiler
 * 
 * @author psy
 * 
 */
public interface LessCompiler {

	/**
	 * Compiles given LESS code to CSS. Beware this method cannot handle file
	 * includes in your LESS code, because it does not know about files.
	 * 
	 * @param lessCode
	 * @return compiled CSS code
	 */
	String compile(String lessCode);

	/**
	 * compiles given LESS file to CSS. This method can handle included files.
	 * 
	 * @param lessFile
	 *            File containing LESS code
	 * @return compiles CSS code
	 */
	String compile(File lessFile);

}
