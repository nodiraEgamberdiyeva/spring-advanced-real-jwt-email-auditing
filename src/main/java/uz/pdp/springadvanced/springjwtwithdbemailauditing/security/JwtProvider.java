package uz.pdp.springadvanced.springjwtwithdbemailauditing.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.springadvanced.springjwtwithdbemailauditing.entity.Role;

import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private static final long expiration= 36000000;
    private static final String key= "secretsectersectet";

    public String generateJwt(String username, List<Role> roles){
        return Jwts
                .builder()
                .signWith(SignatureAlgorithm.HS512, key)
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .setIssuedAt(new Date())
                .compact();
    }

    public String getUsernameFromToken(String token){
        try {
            return Jwts
                    .parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        catch (Exception e){
            return null;
        }

    }
}
