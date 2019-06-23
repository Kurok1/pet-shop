package indi.petshop.producer.exception;

/**
 * token过期异常
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("token已经过期，请重新登录");
    }
}
