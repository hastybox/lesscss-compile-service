LessCss Compile Service
=======================

This small library uses the official Java LessCss Compiler and provides a simple interface
to compile LESS code, LESS files, and LESS files in the servlet context.

The library was created to provide on-the-fly LESS compilation within a controller that
ships the compiled CSS back to the browser. This simplifies LESS development as developers
write LESS code that is published automatically to your servlet container which is integrated
with your IDE (tested on Eclipse + Tomcat). This way there is no need for additional build
management to compile LESS or even manual compilation. 

For production environment a small "caching" layer was added. However this is only optional
and can be replaced with your preferred caching mechanism that is already in place.

Usage
-----

This library was intended to be used with Spring or any other IoC container. It provides
a singleton LessCompileService that should be injected in your services or controller
that need to compile LESS code.

The following is a small example of a Spring configuration and a Spring MVC Controller
to publish compiled LESS files:

### Spring Config
	<bean id="lessCompileService"
		class="com.hastybox.lesscss.compileservice.CachingWebLessCompileServiceWrapper">
		<property name="compileService">
			<bean
				class="com.hastybox.lesscss.compileservice.SimpleWebLessCompileService">
				<property name="basePath" value="/WEB-INF/less/" />
				<property name="lessCompiler">
					<bean
						class="com.hastybox.lesscss.compileservice.compiler.LessCssLessCompilerWrapper">
						<property name="compress" value="true" />
					</bean>
				</property>
			</bean>
 		</property>
	</bean>
	
### Controller code

	@Controller
	public class LessController {
	
		@Autowired
		private WebLessCompileService lessCompileService;
	
		@RequestMapping({ "/css/*.css", "/css/*/*.css" })
		public void retrieveCss(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
			String requestPath = request.getRequestURI();
	
			String regex = "^.+/css/(.*)\\.css";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(requestPath);
	
			if (!matcher.find()) {
				throw new Exception("this is not working");
			}
	
			String filePath = matcher.group(1) + ".less";
	
			String cssCode = lessCompileService.compileFromPath(filePath, request.getSession().getServletContext());
	
			response.getWriter().write(cssCode);
		}
	}

Spring Controller
---------------
As of version 0.2.0 two Spring Controllers are available to handle requests for CSS files and compile the corresponding LESS files.
The UrlBasedSpringLessController is a simple implementation that directly maps a requested CSS file in a configured request path to a LESS file in a pre-configured path within the webapp root. If the file file was not found in the intended path a 404 error is returned. This controller is usefull if you have lots of CSS files that you do not want to configure manually.

	<bean id="lessController" class="com.hastybox.lesscss.compileservice.controller.spring.UrlBasedSpringLessController">
		<property name="basePath" value="WEB-INF/less/" />
		<property name="requestPath" value="/css/" />
		<property name="compileService" ref="lessCompileService" />
	</bean>

The MappedSpringLessController uses a prefedined mapping to map one or more CSS files to a specific LESS file. Regex are used to identify paths to virtual CSS files. If no match was found to the CSS path a 404 error is returned. This controller is usefull if you have a small, defined set of CSS and LESS files.

	<bean id="lessController" class="com.hastybox.lesscss.compileservice.controller.spring.MappedSpringLessController">
		<property name="basePath" value="WEB-INF/less/" />
		<property name="compileService" ref="lessCompileService" />
		<property name="mapping">
			<map>
				<entry key="verysimple\.css" value="simple.less" />
				<entry key="bootstrap/.+css" value="bootstrap/bootstrap.less" />
			</map>
		</property>
	</bean>

The following is an example of how to configure Spring MVC to URL handling on the LESS controllers

	<bean id="lessUrlMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
		<property name="mappings">
			<props>
				<prop key="/css/*.css">lessController</prop>
				<prop key="/css/**/*.css">lessController</prop>
			</props>
		</property>
	</bean>

The controllers can be configured to set the Last-Modified Header to use browser caching. By default the header is set to Jan 1st, 1970.
