package edu.hail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Assert;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

/**
 * Created by wes on 4/8/16.
 */
public class JwtFilter extends GenericFilterBean {
	private static final Log log = LogFactory.getLog(JwtFilter.class);
	protected static final String HEADER_PREFIX = "Bearer ";
	private SecretKey secret;
	
	public void setSecret(SecretKey secret) {
		this.secret = secret;
	}
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpReq = (HttpServletRequest) request;
    	HttpServletResponse httpRes = (HttpServletResponse) response;
        //get token from request object
        String authHeader = httpReq.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith(HEADER_PREFIX)) {
            throw new ServletException("Unable to authorize request, missing token.");
        }

        //Extract token
        String token = authHeader.substring(HEADER_PREFIX.length());
        
        //validate token
        Claims claims = WebUtil.parseToken(token, secret);
        if (claims == null) {
        	httpRes.sendError(HttpServletResponse.SC_FORBIDDEN, "No auth token provided.");
        	throw new ServletException("Invalid token");
        } else {
        	Date now = new Date();
	        Assert.isTrue(claims.getIssuer().equals(WebUtil.APPLICATION_NAME), "Invalid issuer");
	        Assert.isTrue(claims.getIssuedAt().before(now), "Token not yet valid");
	        Assert.isTrue(claims.getExpiration().after(now), "Expired token");
	        httpReq.setAttribute("claims", claims);
	        
	        log.debug(String.format("Request received from [%s]", claims.get("name")));
	    	//continue with other filters
	        chain.doFilter(httpReq, response);
        }
    }
}
