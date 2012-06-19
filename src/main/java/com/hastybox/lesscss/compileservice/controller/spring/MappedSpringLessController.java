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
package com.hastybox.lesscss.compileservice.controller.spring;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author psy
 *
 */
public class MappedSpringLessController extends AbstractSpringLessController {
	
	/**
	 * contains mapping virtual CSS file to LESS file
	 */
	private Map<Pattern, String> mapping;

	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(Map<String, String> mapping) {
		Map<Pattern, String> patternMap = new TreeMap<Pattern, String>();
		for (Entry<String, String> entry : mapping.entrySet()) {
			Pattern pattern = Pattern.compile("^.+" + entry.getKey());
			patternMap.put(pattern, entry.getValue());
		}
		
		this.mapping = patternMap;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String requestUri = request.getRequestURI();
		
		String lessPath = null;
		
		for (Entry<Pattern, String> mappingEntry : mapping.entrySet()) {
			Matcher matcher = mappingEntry.getKey().matcher(requestUri);
			
			if (matcher.find()) {
				lessPath = mappingEntry.getValue();
				break;
			}
			
		}
		
		if (lessPath != null) {
			compileLess(lessPath, response);
		} else {
			response.sendError(404);
		}
		
		return null;
	}

}
