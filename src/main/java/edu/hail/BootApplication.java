package edu.hail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.hail.models.LoginResponse;
import edu.hail.models.LogonData;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
@Configuration
@RequestMapping("/")
public class BootApplication {

    private static final Log log = LogFactory.getLog(BootApplication.class);
    private static String version;
    private static Properties properties;
    protected static SecretKey jwtSecret;

    static {
    	try {
			jwtSecret = KeyGenerator.getInstance("AES").generateKey();
		} catch (NoSuchAlgorithmException e) {
			log.error("Failed to generate secret key for JWT generation/parsing.", e);
		}
    }
    
    //Execution entry point
	public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);

        properties = new Properties();
        try {
        	properties.load(BootApplication.class.getResourceAsStream("/application.properties"));
			version = properties.getProperty("application.version");
		} catch (IOException e) {
			version = "N/A";
			log.error("Unable to read from property files.", e);
		}
        
        // show url to access page (on localhost)
        System.out.println(getLocalUrl());
	}

    /**
     * Filter on all incoming requests (matching the url patterns)
     */
	@Bean
	public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean reg = new FilterRegistrationBean();
        JwtFilter filter = new JwtFilter();
        filter.setSecret(jwtSecret);
        reg.setFilter(filter);
        reg.addUrlPatterns("/api/*");
        
        log.info("JWT filter is registered.");
        return reg;
	}

    /**
     * Read properties file, make string of local url
     * @return local url to access the application in browser
     */
    private static String getLocalUrl() {
        String ret = null;
        ret = String.format("http://localhost:%s%s", properties.getProperty("server.port"), properties.getProperty("server.context-path"));
        return ret;
    }
    
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public @ResponseBody String getAppVersion() {
    	return version;
    }
    
    /**
     * On first load, generate a JWT (JSON Web Token) to be used
     * to authenticate further requests.
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody LoginResponse login(HttpServletResponse response, @RequestBody LogonData data) throws ServletException, IOException {
    	LoginResponse ret = null;
    	if (data.username == null) {
    		//throw new ServletException("User needs identifiable name.");
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User needs identifiable name.");
    	} else {
	    	//Generate token:
	    	String token = WebUtil.generateToken(data.username, jwtSecret);
	    	ret = new LoginResponse(JwtFilter.HEADER_PREFIX + token);
    	}
    	
    	return ret;
    }
}
