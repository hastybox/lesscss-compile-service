package com.hastybox.lesscss.compileservice;

import java.io.File;

public interface LessCompileService {

	/**
	 * compiles given LESS code to CSS
	 * 
	 * @param lessCode
	 * @return compiled LESS code
	 */
	String compileCode(String lessCode);

	/**
	 * compiles given LESS file to CSS
	 * 
	 * @param lessFile
	 * @return compiled LESS code
	 */
	String compileCode(File lessFile);

}