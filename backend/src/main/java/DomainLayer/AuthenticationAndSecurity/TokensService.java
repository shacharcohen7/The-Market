package DomainLayer.AuthenticationAndSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class TokensService {
    private final String SECRET_KEY  ; // Use a strong secret key
    private final long TOKEN_VALIDITY_DURATION = 3600 * 1000; // 1 hour in milliseconds
    private Map<String , String > tokensByMemberId;
    final private Object tokensLock= new Object();

    public TokensService() {
        tokensByMemberId = new HashMap<>();
        SECRET_KEY =generateSecretKey();
        // Constructor
    }

    private String generateSecretKey() {
        try {
            int keyLength = 32;
            SecureRandom secureRandom = SecureRandom.getInstanceStrong(); // Use a strong instance
            byte[] key = new byte[keyLength];
            secureRandom.nextBytes(key);
            return Base64.getEncoder().encodeToString(key);
        }// Encode the key as a Base64 string
        catch (NoSuchAlgorithmException E){
            byte[] array = new byte[32]; // length is bounded by 7
            new Random().nextBytes(array);
            return new String(array, StandardCharsets.UTF_8);
        }
    }



    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            String memberId= getMemberId(token);
            if (expiration.after(new Date())){
                    generateToken(memberId);
                return true;
            }
            else{
                return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getToken(String memberId){
        synchronized (tokensLock) {
            return tokensByMemberId.get(memberId);
        }
    }
    public void removeToken(String memberId){
        synchronized (tokensLock){
            tokensByMemberId.remove(memberId);
        }
    }

    public String getMemberId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public void generateToken(String  memberId) {
        String token = Jwts.builder()
                .subject(memberId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_DURATION))
                .signWith(getSignInKey())
                .compact();
        synchronized (tokensLock){
            tokensByMemberId.put(memberId, token);
        }
        //return
    }


    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] bytes = Base64.getDecoder()
                .decode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(bytes, "HmacSHA256"); }






    /*public String generateToken(String memberId) {
        long now = System.currentTimeMillis();
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Date expiryDate = new Date(now + TOKEN_VALIDITY_DURATION);
        String currToken = Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(new Date(now))
                .expiration(expiryDate)
                .signWith(key)
                .compact();
        synchronized (tokensLock) {
            tokensByMemberId.put(memberId, currToken);
        }
        return currToken;
    }*/

}
