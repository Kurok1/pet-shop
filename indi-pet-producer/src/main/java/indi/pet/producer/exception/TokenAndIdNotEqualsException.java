package indi.pet.producer.exception;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class TokenAndIdNotEqualsException extends RuntimeException{

    public TokenAndIdNotEqualsException() {
        super("token与id不一致");
    }
}
