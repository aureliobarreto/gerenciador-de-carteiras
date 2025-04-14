package com.aurelio.gerenciador_carteiras_api.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aurelio.gerenciador_carteiras_api.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		
		
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);	
			
			String token = JWT.create()
					.withIssuer("gerenciador_carteiras")
					.withSubject(user.getEmail())
					.withExpiresAt(generateExpirationDate())
					.sign(algorithm);
			
			return token;
		}catch(JWTCreationException e) {
			throw new RuntimeException("Error while authenticating");
		}
		
		
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer("gerenciador_carteiras")
					.build()
					.verify(token)
					.getSubject();
			
		} catch(JWTVerificationException e) {
			return null;
		}
	}
	
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.ofHours(-3));
	}

}
