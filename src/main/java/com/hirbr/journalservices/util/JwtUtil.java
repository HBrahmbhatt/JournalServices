package com.hirbr.journalservices.util;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

	private final SecretKey secretKey;

    public JwtUtil(@Value("${security.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

	@SuppressWarnings("deprecation")
	public String generateAccess(String username, List<String> roles, long accessTtlSeconds) {
		return Jwts.builder().setSubject(username).claim("roles", roles).claim("typ", "access").setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(accessTtlSeconds)))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public String extractUsername(String token) {
		return parse(token).getSubject();
	}

	@SuppressWarnings("deprecation")
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	// long lived tokens
	@SuppressWarnings("deprecation")
	public String generateRefresh(String username, long refreshTtlSeconds) {
		try {
			return Jwts.builder().setSubject(username).claim("typ", "refresh").setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(refreshTtlSeconds)))
					.signWith(SignatureAlgorithm.HS256, secretKey).compact();
		} catch (Exception e) {
			log.error("Error while building jwt-->" + e);
			return null;
		}
	}

	public boolean isAccessValid(String token) {
		try {
			return "access".equals(parse(token).get("typ"));
		} catch (Exception e) {
			log.info("Access is not valid-->" + e);
			return false;
		}
	}

	public boolean isRefreshValid(String token) {
		try {
			return "refresh".equals(parse(token).get("typ"));
		} catch (Exception e) {
			log.info("Refresh is not valid-->" + e);
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private Claims parse(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

}
