package java.com.example.demo.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    // 密钥
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // 访问token有效期（毫秒）
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 2 * 60 * 60 * 1000; // 2小时
    
    // 刷新token有效期（毫秒）
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000; // 7天

    /**
     * 生成访问token
     */
    public String generateAccessToken(String userId, String username) {
        return generateToken(userId, username, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成刷新token
     */
    public String generateRefreshToken(String userId, String username) {
        return generateToken(userId, username, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成token
     */
    private String generateToken(String userId, String username, long expireTime) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireTime);
        
        return Jwts.builder()
                .setSubject(userId)
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 解析token
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断token是否即将过期
     */
    public boolean isTokenNearExpiration(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            // 如果token剩余有效期小于30分钟，则视为即将过期
            return expiration.getTime() - System.currentTimeMillis() < 30 * 60 * 1000;
        } catch (Exception e) {
            return true;
        }
    }
} 