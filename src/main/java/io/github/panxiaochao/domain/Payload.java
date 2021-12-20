package io.github.panxiaochao.domain;

import java.util.Date;

/**
 * @author Lypxc
 * @description: TODO
 * @date 2021/12/17 17:31
 */
public class Payload<T> {
    /**
     * token 唯一键
     */
    private String id;
    /**
     * 用户
     */
    private String subject;
    /**
     * token body 数据
     */
    private T data;
    /**
     * 签发时间
     */
    private Date issuedAt;
    /**
     * 过期时间
     */
    private Date expiration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
