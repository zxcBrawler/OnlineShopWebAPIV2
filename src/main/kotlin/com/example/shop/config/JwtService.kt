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

/**
 * Service class for handling JWT (JSON Web Token) operations.
 */
@Service
class JwtService {

    /**
     * Extracts the username from a JWT token.
     *
     * @param jwtToken The JWT token from which to extract the username.
     * @return The extracted username.
     */
    fun extractUsername(jwtToken: String): String {
        // Extracts user's username from generated jwtToken
        return extractClaim(jwtToken, Claims::getSubject)
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param jwtToken The JWT token from which to extract claims.
     * @return The extracted claims.
     */
    fun extractClaims(jwtToken: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigInKey()) // Sets the signing key used to verify the token's signature
            .build()
            .parseClaimsJws(jwtToken)
            .body // Returns the body of the parsed JWT, which contains the claims
    }

    /**
     * Generates a JWT token for a user based on UserDetails.
     *
     * @param userDetails The UserDetails object for the user.
     * @return The generated JWT token.
     */
    fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    /**
     * Generates a JWT token for a user with extra claims.
     *
     * @param extraClaims Additional claims to include in the JWT.
     * @param userDetails The UserDetails object for the user.
     * @return The generated JWT token.
     */
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        // Use the JwtBuilder to construct the JWT token
        return Jwts.builder()
            .setClaims(extraClaims) // Set additional claims if provided
            .setSubject(userDetails.username) // Set the subject of the token (typically the username)
            .setIssuedAt(Date.from(Instant.now())) // Set the token issuance timestamp
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // Set the token expiration timestamp (1 day in the future)
            .signWith(getSigInKey()) // Sign the token with the specified signing key
            .compact() // Compact the token into its final, serialized form
    }
    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param token The JWT token from which to extract the claim.
     * @param claimsResolver The function to resolve the specific claim.
     * @return The extracted claim.
     */
    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        // Extracts all claims from the JWT token
        val claims = extractClaims(token)

        // Applies the provided ClaimsResolver function to obtain the specific claim value
        return claimsResolver.apply(claims)
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token The JWT token to check for expiration.
     * @return `true` if the token has expired, `false` otherwise.
     */
    private fun isTokenExpired(token: String): Boolean {
        // Obtain the expiration date of the token using the extractExpiration function
        val expirationDate = extractExpiration(token)

        // Compare the expiration date with the current date to check if the token is expired
        return expirationDate.before(Date())
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date.
     */
    private fun extractExpiration(token: String): Date {
        // Use the extractClaim function to obtain the expiration claim from the token
        return extractClaim(token, Claims::getExpiration)
    }
    /**
     * Checks if a JWT token is valid.
     *
     * @param jwtToken The JWT token to validate.
     * @param userDetails The UserDetails object for the user.
     * @return `true` if the token is valid, `false` otherwise.
     */
    fun isTokenValid(jwtToken: String, userDetails: UserDetails): Boolean {
        // Extract the username from the JWT token
        val username: String = extractUsername(jwtToken)

        // Check if the extracted username matches the username from the UserDetails
        // Also, ensure that the token is not expired
        return (username == userDetails.username) && !isTokenExpired(jwtToken)
    }

    /**
     * Gets the signing key for JWT operations.
     *
     * @return The SecretKey used for signing and verifying JWTs.
     */
    private fun getSigInKey(): SecretKey {
        // Decode the base64-encoded secret key
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)

        // Create and return a SecretKey for HMAC SHA algorithm
        return Keys.hmacShaKeyFor(keyBytes)
    }
}