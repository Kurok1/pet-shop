package indi.pet.consumer.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.20
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
            if(name.startsWith("indi.pet.producer"))
                name = name.replace("producer", "consumer");
            return Class.forName(name);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return super.resolveClass(desc);
    }
}
