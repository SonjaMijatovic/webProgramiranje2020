package services;

import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import dao.UserDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthenticationService {
	
	@Context
	ServletContext ctx;

	public AuthenticationService() {

	}

	@PostConstruct
	public void init() {

		if (ctx.getAttribute("userDAO") == null) {
			String contextPath = ctx.getRealPath("");
			ctx.setAttribute("userDAO", new UserDao(contextPath));
		}
	}
	
	public static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	
	public static String getUsername(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		System.out.println("Authorization: " + auth);
		if ((auth != null) && (auth.contains("Bearer "))) {
			String jwt = auth.substring(auth.indexOf("Bearer ") + 7);
			try {
				Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
				return claims.getBody().getSubject();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	}

}
