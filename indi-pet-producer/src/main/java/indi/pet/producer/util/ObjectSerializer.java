package indi.pet.producer.util;

import org.apache.kafka.common.serialization.Serializer;

import java.io.*;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class ObjectSerializer implements Serializer<Object> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        byte[] dataArray = null;
        OutputStream outputStream=new ByteArrayOutputStream();
        try{
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(data);
            dataArray=((ByteArrayOutputStream) outputStream).toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }

        return dataArray;
    }

    @Override
    public void close() {

    }
}
