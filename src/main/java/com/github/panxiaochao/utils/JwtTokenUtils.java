package com.github.panxiaochao.utils;

import com.github.panxiaochao.constants.JwtConstants;
import com.github.panxiaochao.domain.Payload;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lypxc
 * @description: JWT Token 工具
 * @date 2021/12/17 17:30
 */
public class JwtTokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    /**
     * 私钥加密token，默认1小时
     *
     * @param subject    登录用户
     * @param object     载荷中的数据
     * @param privateKey 私钥
     * @return
     */
    public static String generateTokenExpireInDefault(String subject, Object object, PrivateKey privateKey) {
        return generateTokenExpireInHours(subject, object, privateKey, JwtConstants.ONE_HOUR);
    }

    /**
     * 私钥加密token，自定义小时
     *
     * @param subject    登录用户
     * @param object     载荷中的数据
     * @param privateKey 私钥
     * @param hours      过期时间，单位小时
     * @return
     */
    public static String generateTokenExpireInHours(String subject, Object object, PrivateKey privateKey, long hours) {
        return generateTokenExpireInMinutes(subject, object, privateKey, hours * 60);
    }

    /**
     * 私钥加密token，自定义分钟
     *
     * @param subject    登录用户
     * @param object     载荷中的数据
     * @param privateKey 私钥
     * @param minutes    过期时间，单位分钟
     * @return
     */
    public static String generateTokenExpireInMinutes(String subject, Object object, PrivateKey privateKey,
                                                      long minutes) {
        return generateTokenExpireInSeconds(subject, object, privateKey, minutes * 60);
    }

    /**
     * 私钥加密token，自定义秒
     *
     * @param subject    登录用户
     * @param object     载荷中的数据
     * @param privateKey 私钥
     * @param seconds    过期时间，单位秒
     * @return
     */
    public static String generateTokenExpireInSeconds(String subject, Object object, PrivateKey privateKey,
                                                      long seconds) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + seconds * 1000);
        //设置头部信息
        Map<String, Object> headerParam = new HashMap<>();
        headerParam.put(JwtConstants.TYP, JwtConstants.JWT);
        headerParam.put(JwtConstants.ALG, JwtConstants.HS256);
        // 设置参数
        JwtBuilder jwtbuilder = Jwts.builder()
                // 设置唯一键值
                .setId(createJWTID())
                // 数据压缩方式
                .compressWith(CompressionCodecs.GZIP)
                // 声明类型jwt
                //.setHeaderParam(JwtConstants.TYP, JwtConstants.JWT)
                .setHeaderParams(headerParam)
                // 面向的用户，作为用户的唯一标志，可以是json或是String
                .setSubject(subject + ":" + expirationDate.getTime())
                // .setAudience("")
                // 签发时间
                .setIssuedAt(createdDate)
                // 过期时间
                .setExpiration(expirationDate)
                // 数据
                .claim(JwtConstants.JWT_CLAIMS, JacksonUtils.toString(object));
        // 签证
        jwtbuilder.signWith(SignatureAlgorithm.RS512, privateKey);
        return jwtbuilder.compact();
    }


    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Claims parserToken(String token, PublicKey publicKey) {
        Claims body = null;
        try {
            body = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.error("parserToken解析异常：{}", e.getMessage());
            return null;
        }
        return body;
    }

    /**
     * 生产唯一ID
     *
     * @return
     */
    private static String createJWTID() {
        return Base64.encodeBase64String(UUID.randomUUID().toString().getBytes());
    }

    /**
     * 获取用户名
     *
     * @param token
     * @param publicKey
     * @return
     */
    public static String getSubject(String token, PublicKey publicKey) {
        Claims body = parserToken(token, publicKey);
        if (body == null) {
            return "";
        }
        return body.getSubject();
    }

    /**
     * 验证token是否合法
     *
     * @param token
     * @return
     */
    public static boolean verifierToken(String token, PublicKey publicKey) {
        Claims body = parserToken(token, publicKey);
        if (body == null) {
            return false;
        }
        return true;
    }

    /**
     * 刷新 token
     *
     * @param token
     * @return
     */
    public static String refreshToken(String token, PublicKey publicKey, PrivateKey privateKey, long seconds) {
        Claims body = parserToken(token, publicKey);
        if (body == null) {
            return "";
        }
        return generateTokenExpireInSeconds(body.getSubject(), body.get(JwtConstants.JWT_CLAIMS), privateKey, seconds);
    }

    /**
     * 验证token是否过期失效
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return
     */
    public static boolean isTokenExpired(String token, PublicKey publicKey) {
        Claims body = parserToken(token, publicKey);
        // 解析异常判定为失效
        if (body == null) {
            return true;
        }
        return body.getExpiration().before(new Date());
    }


    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @param userType  用户类型
     * @return
     */
    public static <T> Payload<T> getBodyFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Claims body = parserToken(token, publicKey);
        Payload<T> payload = new Payload<>();
        if (body == null) {
            return payload;
        }
        payload.setId(body.getId());
        payload.setSubject(body.getSubject());
        payload.setData(JacksonUtils.toBean(body.get(JwtConstants.JWT_CLAIMS).toString(), userType));
        payload.setIssuedAt(body.getIssuedAt());
        payload.setExpiration(body.getExpiration());
        return payload;
    }

    /**
     * 获取token中的用户信息，以 Payload<Map<String, Object>> 返回
     *
     * @param token
     * @param publicKey
     * @return
     */
    public static Payload<Map<String, Object>> getBodyFromToken(String token, PublicKey publicKey) {
        Claims body = parserToken(token, publicKey);
        Payload<Map<String, Object>> payload = new Payload<>();
        if (body == null) {
            return payload;
        }
        payload.setId(body.getId());
        payload.setSubject(body.getSubject());
        payload.setData(JacksonUtils.toMap(body.get(JwtConstants.JWT_CLAIMS).toString()));
        payload.setIssuedAt(body.getIssuedAt());
        payload.setExpiration(body.getExpiration());
        // logger.info(JacksonUtils.toString(payload));
        return payload;
    }
}
