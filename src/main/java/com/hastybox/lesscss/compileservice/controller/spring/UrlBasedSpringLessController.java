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
package com.hastybox.lesscss.compileservice.controller.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring based Controller, that compiles LESS code to CSS on-the-fly
 * 
 * @author psy
 * 
 */
public class UrlBasedSpringLessController extends
		AbstractSpringLessController {

	/**
	 * logger
	 */
	private static final Logger LOGGER;

	static {
		LOGGER = LoggerFactory.getLogger(UrlBasedSpringLessController.class);
	}

	/**
	 * base path to CSS files in request
	 */
	@SuppressWarnings("unused")
	private String requestPath;

	/**
	 * pattern to retrieve path to LESS file
	 */
	private Pattern pathPattern;

	/**
	 * Base path to all CSS files in the request. If all CSS files are in
	 * http://my.domain.com/staticContent/css/*.css {@code requestPath} should
	 * be set to {@code"/staticContent/css/"}. Regex is used to match request
	 * paths. However do not use groups as this implementation tries to fetch
	 * group 1 to find the path to the LESS file to compile.
	 * 
	 * @param requestPath
	 *            the requestPath
	 */
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;

		// compile regex pattern
		this.pathPattern = Pattern.compile("^.+" + requestPath + "(.+)\\.css");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String requestUri = request.getRequestURI();

		LOGGER.debug("Handling request to {}", requestUri);

		Matcher matcher = pathPattern.matcher(requestUri);

		if (!matcher.find()) {
			// no match found, return 404
			LOGGER.info("Request URI {} not mathing pattern, returning 404");

			response.sendError(404);
			return null;
		}

		String resourcePath = matcher.group(1) + ".less";

		// compile the LESS code
		compileLess(resourcePath, response);

		return null;
	}
}
