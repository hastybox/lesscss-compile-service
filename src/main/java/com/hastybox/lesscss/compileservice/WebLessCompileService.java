package com.hastybox.lesscss.compileservice;

import javax.servlet.ServletContext;

public interface WebLessCompileService extends LessCompileService {

	/**
	 * locates LESS file in the servlet context using {@code basePath} as
	 * prefix.
	 * 
	 * @param path
	 * @param context
	 * @return compiled CSS code
	 */
	String compileFromPath(String path, ServletContext context);

}