package io.github.panxiaochao.service.impl;

import io.github.panxiaochao.domain.Payload;
import io.github.panxiaochao.properties.PxcJwtProperties;
import io.github.panxiaochao.service.PxcJwtHandlerService;
import io.github.panxiaochao.utils.JwtTokenUtils;
import io.github.panxiaochao.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * @author Lypxc
 * @description: PxcJwtHandlerService 实现类
 * @date 2021/12/17 17:29
 */
public class PxcJwtHandlerServiceImpl implements PxcJwtHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(PxcJwtHandlerServiceImpl.class);
    private PxcJwtProperties pxcJwtProperties;
    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 私钥
     */
    private PrivateKey privateKey;

    public PxcJwtHandlerServiceImpl(PxcJwtProperties pxcJwtProperties) {
        //logger.info("PxcJwtHandlerServiceImpl...{}", pxcJwtProperties.toString());
        this.pxcJwtProperties = pxcJwtProperties;
        this.publicKey = RsaUtils.getPublicKey(pxcJwtProperties.getPublicKeyStr());
        this.privateKey = RsaUtils.getPrivateKey(pxcJwtProperties.getPrivateKeyStr());
    }

    @Override
    public String createToken(String subject, Object object) {
        return JwtTokenUtils.generateTokenExpireInSeconds(subject, object, privateKey, pxcJwtProperties.getExpireSeconds());
    }

    @Override
    public String verifierToken(String token) {
        return null;
    }

    @Override
    public Payload<Map<String, Object>> parserToken(String token) {
        return JwtTokenUtils.getBodyFromToken(token, publicKey);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return JwtTokenUtils.isTokenExpired(token, publicKey);
    }

    @Override
    public String getSubject(String token) {
        return JwtTokenUtils.getSubject(token, publicKey);
    }

    @Override
    public String refreshToken(String token) {
        return JwtTokenUtils.refreshToken(token, publicKey, privateKey, pxcJwtProperties.getExpireSeconds());
    }


}
