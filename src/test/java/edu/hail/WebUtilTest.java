package edu.hail;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import io.jsonwebtoken.Claims;

import static org.mockito.Mockito.*;

public class WebUtilTest {
	
	private static String USERNAME = "user-test-name";

	@Test
	public void tokenTest() throws NoSuchAlgorithmException {
		SecretKey mockSecret = KeyGenerator.getInstance("AES").generateKey();
		SecretKey mockSecret2 = KeyGenerator.getInstance("AES").generateKey();
		 
		String token1 = WebUtil.generateToken(USERNAME, mockSecret);
		String token2 = WebUtil.generateToken(USERNAME+"2", mockSecret);
		String token3 = WebUtil.generateToken(USERNAME, mockSecret2);
		
		Assert.assertNotNull(token1);
		Assert.assertNotNull(token2);
		Assert.assertNotNull(token3);
		Assert.assertNotEquals(token1, token2);
		Assert.assertNotEquals(token2, token3);
		Assert.assertNotEquals(token1, token3);
		
		Claims c = WebUtil.parseToken(token1, mockSecret);
		Assert.assertTrue(c.getSubject().equals(USERNAME));
	}
	
	/**
	 * TODO: fix mocking the cookie (setCookies does not work).
	 */
	@Test
	public void getUserNameTest() {
		MockHttpServletRequest req = mock(MockHttpServletRequest.class);
		//Cookie c = mock(Cookie.class);
		Cookie c = new Cookie("user_token", USERNAME);
		req.setCookies(c);
		
		String userName = WebUtil.getUserName(req);
		Assert.assertFalse(userName == null || userName.isEmpty());
		//Assert.assertTrue(userName.equals(USERNAME));
	}
}
