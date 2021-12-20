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
     * @param object
     * @return
     */
    String createToken(String subject, Object object);

    /**
     * 检验token合法性
     *
     * @param token
     * @return
     */
    String verifierToken(String token);

    /**
     * 解析token数据
     *
     * @param token
     * @return
     */
    Payload<Map<String, Object>> parserToken(String token);

    /**
     * 验证token是否过期
     *
     * @param token
     * @return
     */
    boolean isTokenExpired(String token);

    /**
     * 获取用户名
     *
     * @param token
     * @return
     */
    String getSubject(String token);

    /**
     * 刷新Token
     *
     * @param token
     * @return
     */
    String refreshToken(String token);
}
