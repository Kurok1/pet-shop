package indi.pet.producer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@RestControllerAdvice
public class HandleExceptionController {

    private Logger logger=LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public Map<String,Object> handle(Exception e){
        logger.error(e.getMessage(),e);
        Map<String,Object> map=new HashMap<>();
        map.put("timestamp",System.currentTimeMillis()/1000);
        map.put("message",e.getMessage());
        map.put("cause",e.getCause());
        return map;
    }

}