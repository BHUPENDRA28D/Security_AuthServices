package UberAuthService.auth.Services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsMutator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService  implements CommandLineRunner {


    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;


    /**
     * This method creates a brand-new JWT token for us based on a payload
     * @return
     */
    public String createToken(Map<String, Object> payload, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry*1000L);
        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .subject(email)
                .signWith(getSignKey())
                .compact();
    }

    public Claims extractAllPayloads(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllPayloads(token);
        return claimsResolver.apply(claims);
    }

    private  Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);

    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * This method checks if the token expiry was before the current time stamp or not ?
     * @param token JWT token
     * @return true if token is expired else false
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Key getSignKey(){
        return  Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    }

    private Boolean validateToken(String token,String email){
        final  String userEmailFetchFromToken = extractEmail(token);
        return(userEmailFetchFromToken.equals(email))&& !isTokenExpired(token);
    }

    private Object extractPayLoad(String token,String payLoadKey){
        Claims claim= extractAllPayloads(token);
        return (Object) claim.get(payLoadKey);

    }


    @Override
    public void run(String... args) throws Exception {

        Map<String ,Object>  map = new HashMap<>();
        map.put("email","bhuendramakode99121@gmail.com");
        map.put("phoneNumber","7854199121");

        String result = createToken(map,"Bhupendra Makode");

        System.out.println("Generated Token  : :   "+result);
        System.out.println("Phone Number : "+extractPayLoad(result,"phoneNumber").toString());
        System.out.println("Email  : "+extractPayLoad(result,"email").toString());
    }
}


