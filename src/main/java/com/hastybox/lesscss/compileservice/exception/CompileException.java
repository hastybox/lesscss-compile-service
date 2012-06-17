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
package com.hastybox.lesscss.compileservice.exception;

/**
 * Exception being thrown on compile errors
 * 
 * @author psy
 *
 */
public class CompileException extends RuntimeException {

	private static final long serialVersionUID = 1956189600399798879L;

	public CompileException(String message) {
		super(message);
	}
	
	public CompileException(Throwable throwable) {
		super(throwable);
	}
	
	public CompileException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
