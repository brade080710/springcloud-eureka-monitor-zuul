package com.jdd.partition.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

public class JwtTokenUtils {

	public static void main(String[] args) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256("secret");
		    String token = JWT.create()
		        .withIssuer("auth0")
		        .sign(algorithm);
		} catch (JWTCreationException exception){
		    //Invalid Signing configuration / Couldn't convert Claims.
		}
	}
}
