package com.gokul.finance.MoneyManager.Util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.el.parser.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

//    private static final String SECRET_KEY_STRING =
//            "a4034fe75f0d958d49013d9c77ae9df899dd309a6ebac7a56eea7ebef2258442";
//
//    private final SecretKey SECRET_KEY =
//            Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
//
//    public String generateToken(UserDetails userDetails) {
//          return Jwts.builder()
//                  .setSubject(userDetails.getUsername())
//                  .setIssuedAt(new Date())
//                  .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60  * 60))
//                  .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
//                  .compact();
//    }
//
//    public boolean validateToken(String Token,UserDetails userDetails) {
//        return extractUserName(Token).equals(userDetails.getUsername());
//    }
//
//    public String extractUserName(String Token) {
//        // Jwts class that parser method that helps to decerept the token and the value from token
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)   // same key used to sign the token
//                .build()
//                .parseClaimsJws(Token)       // decrypt & validate the token
//                .getBody()
//                .getSubject();
//
//    }

    private static final String SECRET_KEY_STRING =
            "YTQwMzRmZTc1ZjBkOTU4ZDQ5MDEzZDljNzdhZTlkZjg5OWRkMzA5YTZlYmFjN2E1NmVlYWE3ZWJlZjIyNTg0NDI=";

    private final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_STRING));

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUserName(token).equals(userDetails.getUsername());
    }

    public String extractUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
