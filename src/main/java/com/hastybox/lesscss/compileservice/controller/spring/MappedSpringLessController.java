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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

/**
 * This Controller uses a defined map of CSS regex request URIs to map to
 * specific LESS files. If one of the regex patterns matches, the corresponding
 * LESS file is being compiled and returned.
 * 
 * @author psy
 * 
 */
public class MappedSpringLessController extends AbstractSpringLessController {

	/**
	 * logger
	 */
	private static final Logger LOGGER;

	static {
		LOGGER = LoggerFactory.getLogger(MappedSpringLessController.class);
	}

	/**
	 * contains mapping virtual CSS file to LESS file
	 */
	private Map<Pattern, String> mapping;

	/**
	 * Mapping (path to CSS in request) => (path to LESS file). BEWARE: The key
	 * is compiled to a regex. The value however must be a concrete path.
	 * 
	 * @param mapping
	 *            the mapping to set
	 */
	public void setMapping(Map<String, String> mapping) {
		Map<Pattern, String> patternMap = new HashMap<Pattern, String>();
		for (Entry<String, String> entry : mapping.entrySet()) {
			Pattern pattern = Pattern.compile("^.+" + entry.getKey());
			patternMap.put(pattern, entry.getValue());
		}

		this.mapping = patternMap;
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

		LOGGER.debug("Handling request to URI {}" + requestUri);

		String lessPath = null;

		for (Entry<Pattern, String> mappingEntry : mapping.entrySet()) {
			Matcher matcher = mappingEntry.getKey().matcher(requestUri);

			if (matcher.find()) {
				lessPath = mappingEntry.getValue();

				LOGGER.debug("Matched {} to {}", requestUri, lessPath);

				break;
			}

		}

		if (lessPath != null) {
			compileLess(lessPath, response);
		} else {
			// send 404 if no match was found
			LOGGER.info("Could not match {}. Sending 404.", requestUri);
			response.sendError(404);
		}

		return null;
	}

}
