package UberAuthService.auth.Services;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService  implements CommandLineRunner {


    @Value("${jwt.expiry}")
    private int expiry;
    @Value("${jwt.secret}")
    private  String SECRET;


    /*
    * This methode creates a brand-ee  new JWT token for us based on payload!
    * */
    private String createToken(Map<String ,Object> payLoad,String username){

        Date now =new Date();
        Date expiryDAte =new Date(now.getTime()+expiry+1000L);
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(payLoad)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDAte)
                .subject(username)
                .signWith(key,Jwts.SIG.HS256)  // new correct signature.
                .compact();
    }

    @Override
    public void run(String... args) throws Exception {

        Map<String ,Object>  map = new HashMap<>();
        map.put("email","bhuendramakode99121@gmail.com");
        map.put("phoneNo : ","7854199121");

        String result = createToken(map,"Bhupendra Makode");

        System.out.println("Generated Token  : :   "+result);
    }
}


