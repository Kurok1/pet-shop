package indi.pet.producer.util;

import java.io.File;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class FileUtils {

    public static void deleteFile(String path){
        String fullPath=Thread.currentThread().getContextClassLoader().getResource("/").getPath()+"/"+path;
        File file=new File(fullPath);
        if(file.exists())
            file.delete();
        file=null;
    }

}
