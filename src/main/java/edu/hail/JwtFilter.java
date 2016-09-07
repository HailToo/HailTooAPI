package edu.hail;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by wes on 4/8/16.
 */
public class JwtFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //get token from request object
        String token = ((HttpServletRequest) request).getHeader("jwtoken");
        if(token == null || token.trim().isEmpty()) {
            throw new ServletException("Unable to authorize request, missing token.");
        }

        //TODO
        //validate token
        //set iss (issuer)

        //continue with other filters
        chain.doFilter(request, response);
    }
}
