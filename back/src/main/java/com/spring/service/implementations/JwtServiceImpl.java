package com.spring.service.implementations;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.service.JwtService;
import com.spring.constants.SecurityConstants;
import com.spring.enums.Role;
import com.spring.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private final PropertiesService propertiesService;

	@Autowired
	public JwtServiceImpl(PropertiesService propertiesService, UserService userService) {
		this.propertiesService = propertiesService;
	}

	@Override
	public String generateToken(String username, Set<String> roleSet) {
		LOGGER.info("Creating token with data: Username:" + username + " Role:" + (roleSet.contains(Role.ADMIN.name()) ? "ADMIN" : "USER"));
		return Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(propertiesService.getJwtSecret().getBytes()), SignatureAlgorithm.HS512)
				.setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
				.setIssuer(SecurityConstants.TOKEN_ISSUER)
				.setAudience(SecurityConstants.TOKEN_AUDIENCE)
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
				.claim("rol", roleSet)
				.compact();
	}

	@Override
	@Transactional
	public UsernamePasswordAuthenticationToken parseToken(String token) {

		byte[] secret = propertiesService.getJwtSecret().getBytes();
		Jws<Claims> jwtsToken;

		try {
			jwtsToken = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
		} catch(Exception e) {
			LOGGER.info(e.getMessage());
			return null;
		}

		String username = jwtsToken.getBody().getSubject();

		if(username == null) {
			return null;
		}

		LOGGER.info("Parsed token for user : " + username);

		Set<Role> roles = ((List<?>) jwtsToken.getBody().get("rol"))
				.stream()
				.map(x -> Role.valueOf((String)x))
				.collect(Collectors.toSet());

		return new UsernamePasswordAuthenticationToken(username, null, roles);
	}
}
