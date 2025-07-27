package service.Jwt.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import service.Jwt.JwtService;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;


@Service
public class JwtImpl implements JwtService {
    private String secretkey = "";

    public JwtImpl() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generateToken(String email,String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // ✅ Add role to claims
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(getKey())
                .compact();

    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}




//@Service
//public class JwtImpl implements JwtService {
//
//    @Value("${jwt.secret}")  // ✅ Inject secret from properties
//    private String secretkey;
//
//    @Override
//    public String generateToken(String email, String role) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", role); // ✅ Add role to claims
//        return Jwts.builder()
//                .claims()
//                .add(claims)
//                .subject(email)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
//                .and()
//                .signWith(getKey())
//                .compact();
//    }
//
//    private Key getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
//        return Keys.hmacShaKeyFor(keyBytes); // ✅ Requires exactly 256-bit key
//    }

