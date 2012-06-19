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

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.hastybox.lesscss.compileservice.exception.CompileException;

/**
 * Spring based Controller, that compiles LESS code to CSS on-the-fly
 * 
 * @author psy
 * 
 */
public class UrlBasedSpringLessCssController extends AbstractSpringLessController  {

	/**
	 * base path to CSS files in request
	 */
	private String requestPath;
	
	/**
	 * pattern to retrieve path to LESS file
	 */
	private Pattern pathPattern;

	/**
	 * Base path to all CSS files in the request. If all CSS files are in
	 * http://my.domain.com/staticContent/css/*.css {@code requestPath} should be set to
	 * {@code"/staticContent/css/"}.
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
		
		Matcher matcher = pathPattern.matcher(request.getRequestURI());
		
		if (!matcher.find()) {
			throw new CompileException("Could not find path to LESS file in URL");
		}
		
		String resourcePath = matcher.group(1) + ".less";
		
		// compile the LESS code
		compileLess(resourcePath, response);
		
		return null;
	}
}
