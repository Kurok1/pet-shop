package indi.pet.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.24
 */
@RestControllerAdvice
public class HandleExceptionController {

    private Logger logger=LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public Map<String,Object> handle(Exception e, HttpServletResponse response){
        logger.error(e.getMessage(),e);
        Map<String,Object> map=new HashMap<>();
        map.put("flag",false);
        map.put("timestamp",System.currentTimeMillis()/1000);
        map.put("message",e.getMessage());
        map.put("cause",e.getCause());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return map;
    }
}
