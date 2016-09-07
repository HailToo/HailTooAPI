package edu.hail.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Log log = LogFactory.getLog(AdminController.class);
    private static String secretKey;

    static {
        Properties properties = new Properties();
        try {
            properties.load(AdminController.class.getResourceAsStream("/secret.properties"));
            secretKey = properties.getProperty("jwt.secret");
        } catch(IOException ex) {
            log.error("Failed to read token secret key from properties file, using default value.", ex);
            secretKey = "defaultKey";
        }
    }

    public @ResponseBody LoginResponse login(@RequestBody LogonData login) throws ServletException {
        if(!login.isValid()) {
            throw new ServletException("Missing required login credentials.");
        }

        //Authenticate user against DB

        Date now = new Date();

        //Create token
        String token = Jwts.builder().setSubject(login.username)
                .setIssuedAt(now)
                .setExpiration(addTime(now, 3600))
                .setPayload("Payload? PAYLOAD?  WE'RE TALKING ABOUT PAYLOADS?")
                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();

        return new LoginResponse(token);
    }

    private static Date addTime(Date startTime, int secondsToAdd) {
        long endTime = startTime.getTime() + (1000*secondsToAdd);
        return new Date(endTime);
    }

    static class LogonData {
        public String username;
        public String password;
        public String uri;

        public boolean isValid() {
            return !(username == null && password == null && uri == null
                && username.trim().isEmpty() && password.trim().isEmpty() && uri.trim().isEmpty());
        }
    }

    static class LoginResponse {
        public String token;
        public LoginResponse(String token) {
            this.token = token;
        }
    }
}
