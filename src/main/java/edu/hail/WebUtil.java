package edu.hail;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

import edu.hail.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class WebUtil {
	private static final Log log = LogFactory.getLog(WebUtil.class);
	private static Gson gson = new Gson();
	protected static final String APPLICATION_NAME = "HailToo";
	
	public static String getUserName(HttpServletRequest req) {
		if (req  == null) {
			throw new IllegalArgumentException("Parameter of type HttpServletRequest must not be null.");
		}
    	String name = "N/A";
    	Claims claims = (Claims) req.getAttribute("claims");
    	if (claims != null) {
    		name = claims.getSubject();
    	} else {
    		log.info("Unable to detect username from HTTP request.");
    	}
    	
    	return name;
    }
	
	public static String generateToken(String subject, SecretKey secret) {
    	String ret = "";
    	Date now = new Date();
    	
    	ret = Jwts.builder()
    			.setSubject(subject)
    			.setIssuer(APPLICATION_NAME)
    			.setIssuedAt(now)
                .setExpiration(addTime(now, 3600))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    	log.info("Generated token: " + ret);
    	return ret;
    }
	
	public static Claims parseToken(String token, SecretKey secret) {
		log.debug("Parsing token: " + token);
		Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		//User u = new User(body.getSubject(), "");
		//return u;
		log.debug(String.format("token [%s] parsed into object: [%s]", token, gson.toJson(body)));
		return body;
	}
	
	private static Date addTime(Date startTime, int secondsToAdd) {
        long endTime = startTime.getTime() + (1000*secondsToAdd);
        return new Date(endTime);
    }
	
	public static User getUser(HttpServletRequest req) {
		User ret = new User(getUserName(req), "");
		//TODO other information from token to User (if we want any).
		return ret;
	}
}
