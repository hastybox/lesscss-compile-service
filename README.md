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