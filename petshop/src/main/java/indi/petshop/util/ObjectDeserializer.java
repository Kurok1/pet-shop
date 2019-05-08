package indi.petshop.util;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class ObjectDeserializer implements Deserializer<Object> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Serializable deserialize(String topic, byte[] data) {

        Serializable object=null;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

        try {
            ObjectInputStream inputStream = new OrderObjectInputStream(byteArrayInputStream);

            object = (Serializable) inputStream.readObject();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return object;
    }

    @Override
    public void close() {

    }
}
