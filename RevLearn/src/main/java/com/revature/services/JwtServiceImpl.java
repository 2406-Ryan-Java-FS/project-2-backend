package com.revature.services;

import com.revature.exceptions.BadRequestException;
import com.revature.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService{
    public static final long EXPIRATION = 12 * 60 * 60 * 1000;          // expiration = 12 hours

    @Autowired
    UserService us;

    @Value("${jwt.secret}")
    private String secretKey;                                           // get secretkey from application.yml


    /**
     * Calls private JWT token generator with a subject of userid
     *
     * @param id The userId of a User.
     * @return jwt token as String.
     */
    @Override
    public String generateJwt(Integer id) {
        return generateJwtHelper(id);
    }

    /**
     * Generates JWT token with a subject of userid
     *
     * @param id The userId of a User.
     * @return jwt token as String.
     */
    private String generateJwtHelper(Integer id) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));   // create key from app.yml

        return Jwts.builder()
                .subject(id.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * Get user from the token
     *
     * @param token the jwt token from request
     * @return User if exists or null if not.
     */
    @Override
    public User getUserFromToken(String token){
        int id = verifyJwt(token);
        if(id == 0){
            System.out.println("Authentication Failed");
            return null;
        } else if(id == -1) {
            System.out.println("Token Expired");
            return null;
        } else{
            return us.getUser(id);
        }
    }

    /**
     * Verify JWT token
     *
     * @param token the jwt token from request
     * @return 0  = Auth failed (token is not valid)
     *        -1  = expired token
     *         1+ = id of user
     */
    @Override
    public int verifyJwt(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));   // create key from app.yml
        token = token.split(" ")[1].trim();                                      //remove "Bearer" from token header
        Claims body;
        try {
            body = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            return 0;
        }
        if(body.getExpiration().getTime() < System.currentTimeMillis()) {
            return -1;
        }

        return Integer.parseInt(body.getSubject());
    }

}
