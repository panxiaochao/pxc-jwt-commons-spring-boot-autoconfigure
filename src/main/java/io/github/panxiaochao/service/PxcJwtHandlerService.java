package io.github.panxiaochao.service;

import io.github.panxiaochao.domain.Payload;

import java.util.Map;

/**
 * @author Lypxc
 * @description: TODO
 * @date 2021/12/17 17:16
 */
public interface PxcJwtHandlerService {

    /**
     * 传入对象
     *
     * @param subject 用户名
     * @param object  对象
     * @return String
     */
    String createToken(String subject, Object object);

    /**
     * 检验token合法性
     *
     * @param token token
     * @return String
     */
    String verifierToken(String token);

    /**
     * 解析token数据
     *
     * @param token token
     * @return String
     */
    Payload<Map<String, Object>> parserToken(String token);

    /**
     * 验证token是否过期
     *
     * @param token token
     * @return String
     */
    boolean isTokenExpired(String token);

    /**
     * 获取用户名
     *
     * @param token token
     * @return String
     */
    String getSubject(String token);

    /**
     * 刷新Token
     *
     * @param token token
     * @return String
     */
    String refreshToken(String token);
}
