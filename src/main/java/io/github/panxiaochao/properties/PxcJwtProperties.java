package io.github.panxiaochao.properties;

import io.github.panxiaochao.banner.PxcJwtBanner;
import io.github.panxiaochao.utils.RsaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Lypxc
 * @description: JWT 配置文件
 * @date 2021/12/17 13:31
 */
@ConfigurationProperties(prefix = PxcJwtProperties.PXCJWT_PREFIX, ignoreInvalidFields = true)
public class PxcJwtProperties implements InitializingBean {
    /**
     * 属性前缀
     */
    public static final String PXCJWT_PREFIX = "pxcjwt";
    /**
     * 默认秘钥
     */
    public static final String DEFAULT_SECRET = "pxc-jwt-rsa-secret";
    /**
     * 公钥，Base64字符串
     */
    private String publicKeyStr;
    /**
     * 私钥，Base64字符串
     */
    private String privateKeyStr;
    /**
     * 密匙
     */
    private String secret = DEFAULT_SECRET;
    /**
     * 失效时间，默认3600秒，即1小时
     */
    private long expireSeconds = 3600L;
    /**
     * 是否显示自定义 banner
     */
    private Boolean showBanner = Boolean.TRUE;

    public String getPublicKeyStr() {
        return publicKeyStr;
    }

    public void setPublicKeyStr(String publicKeyStr) {
        this.publicKeyStr = publicKeyStr;
    }

    public String getPrivateKeyStr() {
        return privateKeyStr;
    }

    public void setPrivateKeyStr(String privateKeyStr) {
        this.privateKeyStr = privateKeyStr;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(long expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public Boolean getShowBanner() {
        return showBanner;
    }

    public void setShowBanner(Boolean showBanner) {
        this.showBanner = showBanner;
    }

    @Override
    public String toString() {
        return "PxcJwtProperties{" +
                "publicKeyStr='" + publicKeyStr + '\'' +
                ", privateKeyStr='" + privateKeyStr + '\'' +
                ", secret='" + secret + '\'' +
                ", expireSeconds=" + expireSeconds +
                ", showBanner=" + showBanner +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isAllBlank(publicKeyStr, privateKeyStr)) {
            Map<String, String> rsaMap = RsaUtils.initKeyBase64Str(this.secret);
            this.publicKeyStr = rsaMap.get("publicKey");
            this.privateKeyStr = rsaMap.get("privateKey");
        }
        // print banner
        if (this.showBanner) {
            PxcJwtBanner jwtBanner = new PxcJwtBanner();
            jwtBanner.printBanner();
        }
    }
}
