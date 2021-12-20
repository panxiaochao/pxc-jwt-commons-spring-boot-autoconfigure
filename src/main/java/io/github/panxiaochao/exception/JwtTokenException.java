package io.github.panxiaochao.exception;

/**
 * @author Mr_LyPxc
 * @title: JwtTokenException
 * @description: 错误自定义
 * @date 2021/12/18 14:07
 */
public class JwtTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JwtTokenException() {
        super();
    }

    public JwtTokenException(String message) {
        super(message);
    }

    public JwtTokenException(Throwable cause) {
        super(cause);
    }

    public JwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
