package indi.pet.producer.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 用于解决序列化前后包名的问题
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.03.04
 */
public class OrderObjectInputStream extends ObjectInputStream {
    protected OrderObjectInputStream() throws IOException, SecurityException {
        super();
    }

    public OrderObjectInputStream(InputStream arg0) throws IOException {
        super(arg0);
    }

    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException{
       String name = desc.getName();
       try {
            if(name.startsWith("indi.pet.consumer"))
                name = name.replace("consumer", "producer");
            return Class.forName(name);

       } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
       }

       return super.resolveClass(desc);
    }

}
