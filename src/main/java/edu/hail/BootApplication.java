package edu.hail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@EnableAutoConfiguration
public class BootApplication {

    private static final Log log = LogFactory.getLog(BootApplication.class);

    //Execution entry point
	public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);

        // show url to access page (on localhost)
        System.out.println(getLocalUrl());
	}

    /**
     * Filter on all incoming requests (matching the url patterns)
     */
	@Bean
	public FilterRegistrationBean jwtFilter() {
        FilterRegistrationBean reg = new FilterRegistrationBean();
        reg.setFilter(new JwtFilter());
        reg.addUrlPatterns("/api");
        return reg;
	}

    /**
     * Read properties file, make string of local url
     * @return local url to access the application in browser
     */
    private static String getLocalUrl() {
        String ret = null;
        Properties prop = new Properties();
        try {
            prop.load(BootApplication.class.getResourceAsStream("/application.properties"));
            ret = String.format("http://localhost:%s%s", prop.getProperty("server.port"), prop.getProperty("server.context-path"));
        } catch (IOException ex) {
            log.error("Unable to read properties to get local URL of running application.");
            ret = "N/A";
        }
        return ret;
    }
}
