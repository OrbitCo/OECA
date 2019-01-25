package gov.epa.oeca.common.security.jwt;

import gov.epa.oeca.common.security.ApplicationUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dfladung
 */
@Component
public class JwtTokenUtil {

    public static final String JWT_TOKEN_COOKIE_NAME = "oeca-jwt";
    public static final Integer JWT_COOKIE_EXPIRATION_S = 60 * 20; // 20 minutes
    public static final Long JWT_TOKEN_EXPIRATION_MS = 1000L * JWT_COOKIE_EXPIRATION_S; // 20 minutes
    public static final String JWT_TOKEN_HEADER = "Authorization";

    String secret;


    public JwtTokenUtil() {
        secret = "replaceMeEventually";
    }

    public ApplicationUser parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        String roleString = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody().get("roles", String.class);
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        if (!StringUtils.isEmpty(roleString)) {
            String[] roleValues = StringUtils.split(roleString, ",");
            for (int i = 0; i < roleValues.length; i++) {
                roles.add(new SimpleGrantedAuthority(roleValues[i]));
            }
        }
        return new ApplicationUser(username, token, roles);
    }

    public String createTokenForUser(String userName) {
        return createTokenForUser(userName, null);
    }

    public String createTokenForUser(String userName, List<String> roles) {
        return Jwts.builder()
                .setSubject(userName)
                .claim("roles", StringUtils.join(roles, ","))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String refreshToken(Authentication authentication) {
        return createTokenForUser(authentication.getName());
    }

    public void insertTokenIntoCookie(String token, HttpServletResponse response) {
        // setting it this way because chrome doesn't seem to respect the traditional way
        Cookie cookie = new Cookie(JwtTokenUtil.JWT_TOKEN_COOKIE_NAME, token);
        cookie.setPath("/"); // set for the current domain
        cookie.setMaxAge(JWT_COOKIE_EXPIRATION_S); // delete when browser closes
        response.addCookie(cookie);
    }

    public void insertTokenIntoHeader(String token, HttpServletResponse response) {
        response.setHeader(JWT_TOKEN_HEADER, String.format("Bearer %s", token));
    }


    public String extractTokenFromCookie(HttpServletRequest req) {
        String result = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtTokenUtil.JWT_TOKEN_COOKIE_NAME)) {
                    result = cookie.getValue();
                    break;
                }
            }
        }
        return result;
    }

    public String extractTokenFromHeader(HttpServletRequest req) {
        String token = null;
        String header = req.getHeader(JWT_TOKEN_HEADER);
        if (!StringUtils.isEmpty(header)) {
            if (StringUtils.startsWith(header.trim(), "Bearer")) {
                token = StringUtils.remove(header, "Bearer").trim();
            }
        }
        return token;
    }
}
