package indi.pet.consumer.exception;

/**
 * token超时异常
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException() {
        super("token已经过期，请重新登录");
    }

}
