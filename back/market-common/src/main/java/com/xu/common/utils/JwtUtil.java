package com.xu.common.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @author AITIAN
 * 通过jwt生成用户token
 */
@Component
public class JwtUtil {

    /**
     * 设置token有效时长一小时
     */
    private static final long TIME = 1000 * 60 * 60;
    private static final String SECRET = "admin123";

    @Resource
    private  RedisUtil redisUtil;

    public static String createToken(String userName, Object userId) {
        JwtBuilder builder = Jwts.builder();
        return builder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .claim("username", userName)
                .claim("userId", userId)
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis() + TIME))
                .setId(UUID.randomUUID().toString())
                //  signature
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static Boolean checkToken(String token, String userName, Integer userId) {
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(SECRET).parseClaimsJws(token);
        Claims jwsBody = claimsJws.getBody();
        return jwsBody.get("username").equals(userName) && jwsBody.get("userId").equals(userId) ;
    }

    public static Boolean checkToken(String token, String userName, String userUid) {
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(SECRET).parseClaimsJws(token);
        Claims jwsBody = claimsJws.getBody();
        return jwsBody.get("username").equals(userName) && jwsBody.get("userId").equals(userUid) ;
    }

    public Boolean checkUserToken(int userId, String userName, String userToken, int flag){
        Object token = redisUtil.get("userTokenBefore:" + userId);
        Boolean checkToken = false;
        if (token != null && flag == 1) {
            if (!"".equals(userToken) && token.equals(userToken)){
                checkToken = JwtUtil.checkToken((String) token, userName, userId);
            }
        }
        return checkToken;
    }

    public Boolean checkUserToken(String userUid, String userName, String userToken){
        Object token = redisUtil.get("userTokenBack:" + userUid);
        boolean checkToken = false;
        if (token != null) {
            if (!userToken.isEmpty() && token.equals(userToken)){
                checkToken = JwtUtil.checkToken((String) token, userName, userUid);
            }
        }
        return checkToken;
    }

}
