package com.hastybox.lesscss.compileservice.controller.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.LastModified;

import com.hastybox.lesscss.compileservice.LessCompileService;

abstract public class AbstractSpringLessController implements Controller,
		LastModified, ResourceLoaderAware {
	/**
	 * logger
	 */
	private static final Logger LOGGER;
	
	static {
		LOGGER = LoggerFactory.getLogger(AbstractSpringLessController.class);
	}

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

	/**
	 * Compiles CSS code from given file in {@code resourcePath} and writes it
	 * into the given {@code response} object. If the file was not found a 404
	 * response is triggered.
	 * 
	 * 
	 * @param resourcePath
	 *            path to file to compile
	 * @param response
	 *            response to write to
	 * @throws IOException
	 */
	protected void compileLess(String resourcePath, HttpServletResponse response)
			throws IOException {
		LOGGER.debug("Compiling LESS from {}", resourcePath);
		
		String cssCode;

		try {
			// try to compile file
			cssCode = compileLess(resourcePath);

		} catch (FileNotFoundException e) {
			// file not found. Set ressponse to 404
			LOGGER.info("LESS file at {} not found. Returning 404.", resourcePath);
			
			response.sendError(404);
			return;
		}

		// write to response
		response.getWriter().write(cssCode);

		response.setContentType("text/css");
	}

	/**
	 * compiles CCS code from given file in {@code resourcePath}
	 * 
	 * @param resourcePath
	 *            path to LESS file
	 * @return compiles CSS code
	 * @throws IOException
	 */
	protected String compileLess(String resourcePath) throws IOException {
		// load LESS file
		Resource resource = resourceLoader.getResource(basePath + resourcePath);

		if (!resource.exists()) {
			throw new FileNotFoundException("Resouce at " + resourcePath
					+ " does not exist");
		}

		File lessFile = resourceLoader.getResource(basePath + resourcePath)
				.getFile();

		// compile
		return compileService.compileCode(lessFile);
	}

}