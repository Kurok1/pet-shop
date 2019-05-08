package indi.petshop.chatting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.petshop.chatting.entity.LeaveInfo;
import indi.petshop.chatting.entity.Message;

import java.io.IOException;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
public class MessageUtil {

    private static ObjectMapper mapper;

    static {
        mapper=new ObjectMapper();
    }

    public static Message toMessage(String message){
        if(mapper==null)
            mapper=new ObjectMapper();
        Message result=null;
        try {
            result = mapper.readValue(message,Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String toString(Message message){
        String result=null;
        try {
            result = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String toString(LeaveInfo info){
        String result=null;
        try {
            result = mapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
