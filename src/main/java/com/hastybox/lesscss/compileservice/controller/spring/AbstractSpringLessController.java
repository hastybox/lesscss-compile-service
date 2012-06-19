package com.hastybox.lesscss.compileservice.controller.spring;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.LastModified;

import com.hastybox.lesscss.compileservice.LessCompileService;

abstract public class AbstractSpringLessController implements Controller, LastModified,
		ResourceLoaderAware {

	/**
	 * resource loader to load LESS files
	 */
	protected ResourceLoader resourceLoader;
	/**
	 * compile service to do the compilation
	 */
	protected LessCompileService compileService;
	/**
	 * base path of all LESS files
	 */
	protected String basePath;

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;

	}

	/**
	 * @param compileService
	 *            the compileService to set
	 */
	public void setCompileService(LessCompileService compileService) {
		this.compileService = compileService;
	}

	/**
	 * Base path to all LESS files. Be aware that a
	 * {@link org.springframework.core.io.ResourceLoader} is used to access LESS
	 * files (e.g. "WEB-INF/less/", beware closing "/").
	 * 
	 * @param basePath
	 *            the basePath
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public long getLastModified(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	protected void compileLess(String resourcePath, HttpServletResponse response) throws IOException {
		String cssCode = compileLess(resourcePath);
		
		// write to response
		response.getWriter().write(cssCode);
	}
	
	protected String compileLess(String resourcePath) throws IOException {
		// load LESS file
		File lessFile = resourceLoader.getResource(basePath + resourcePath).getFile();
		
		// compile
		return compileService.compileCode(lessFile);
	}

}