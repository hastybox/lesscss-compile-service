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

/**
 * Abstract class containing common functions to SpringLessControllers
 * @author psy
 *
 */
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

	/**
	 * relative value (from now) to set to LastModified header.
	 */
	private Long relativeLastModified;

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
	 * Relative value (from now) to set to LastModified Header. {@code null}
	 * will always return Jan. 1st, 1970 in header. A positive value will set
	 * the LastModied to be {@code relativeLastModified} milliseconds in the
	 * past. A negative value will always trigger a generation of the content.
	 * 
	 * 
	 * @param relativeLastModified
	 *            the relativeLastModified to set
	 */
	public void setRelativeLastModified(Long relativeLastModified) {
		this.relativeLastModified = relativeLastModified;
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
		if (relativeLastModified == null) {
			return 0;
		}
		
		if (relativeLastModified < 0L) {
			return -1;
		}
		
		return System.currentTimeMillis() - relativeLastModified;
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

		// TODO add caching here to prevent file system lookup
		try {
			// try to compile file
			cssCode = compileLess(resourcePath);

		} catch (FileNotFoundException e) {
			// file not found. Set ressponse to 404
			LOGGER.info("LESS file at {} not found. Returning 404.",
					resourcePath);

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
			throw new FileNotFoundException("Resource at " + resourcePath
					+ " does not exist");
		}

		File lessFile = resourceLoader.getResource(basePath + resourcePath)
				.getFile();

		// compile
		return compileService.compileCode(lessFile);
	}

}