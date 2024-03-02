package com.example.shop.config

import com.example.shop.config.Constants.SECRET_KEY
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.function.Function

import javax.crypto.SecretKey

@Service
class JwtService {


    fun extractUsername(jwtToken: String): String {
        return extractClaim(jwtToken, Claims::getSubject)
    }

    fun extractClaims(jwtToken: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigInKey())
            .build()
            .parseClaimsJws(jwtToken)
            .body
    }

    private fun getSigInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
            .signWith(getSigInKey())
            .compact()
    }

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    fun isTokenValid(jwtToken: String, userDetails: UserDetails): Boolean {
        val username: String = extractUsername(jwtToken)
        return (username == userDetails.username) && !isTokenExpired(jwtToken)
    }
}