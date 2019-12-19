package co.jratil.blogsecurity.util;

import co.jratil.blogsecurity.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-06 17:44
 */
public class JwtUtils {

    private static String secretKey;

    static {
        Properties prop = new Properties();
        try {
            prop.load(JwtUtils.class.getResourceAsStream("/prop/secret-key.properties"));
            secretKey = Base64.getEncoder().encodeToString(prop.getProperty("jwt.secret-key").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static byte[] bytes = secretKey.getBytes();
    private static Key key = Keys.hmacShaKeyFor(bytes);

    /**
     * 将用户id和用户角色一起创建token
     *
     * @param authorId
     * @param roles
     * @param rememberMe 是否记住我，根据此变量来决定 token 过期时间
     * @return
     */
    public static String createToken(Integer authorId, List<String> roles, boolean rememberMe) {
        long expire = JwtConstant.EXPIRATION;
        if (rememberMe) {
            expire = JwtConstant.EXPIRATION_REMEMBER;
        }

        String token = Jwts.builder()
                .setHeaderParam("typ", JwtConstant.TOKEN_TYPE)
                .signWith(key)
                .claim(JwtConstant.ROLE_CLAIMS, String.join(",", roles))
                .setIssuer("admin")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))
                .setSubject(String.valueOf(authorId))
                .compact();

        return JwtConstant.TOKEN_PREFIX + token;
    }

    public static boolean isExpire(String toke) {
        Claims claims = getBody(toke);
        return claims.getExpiration().getTime() < new Date().getTime();
    }

    /**
     * 从token获取用户id
     *
     * @param token
     * @return
     */
    public static Integer getIdByToken(String token) {
        return Integer.valueOf(getBody(token).getSubject());
    }

    /**
     * 从token中获取用户角色
     *
     * @param token
     * @return
     */
    public static List<GrantedAuthority> getRolesByToken(String token) {
        String roles = (String) getBody(token)
                .get(JwtConstant.ROLE_CLAIMS);

        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private static Claims getBody(String token) {
        token = token.replace(JwtConstant.TOKEN_PREFIX, "");
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }
}
