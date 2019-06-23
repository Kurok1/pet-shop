package indi.petshop.chatting.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
public class MD5Util {
    private static String toHex(byte[] bytes){
        final char[] HEX_DIGITS="0123456789ABCDEF".toCharArray();
        StringBuilder ret=new StringBuilder(bytes.length*2);
        for (int i=0;i<bytes.length;i++){
            ret.append(HEX_DIGITS[(bytes[i]>>4)&0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 获取一个字符串的MD5码
     * @param source 源字符串
     * @return 根据源字符串生成的MD5值
     * @throws NoSuchAlgorithmException 获取md5对象时失败
     * @throws UnsupportedEncodingException 编码错误
     */
    public static String getMD5Code(String source) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        return toHex(md5.digest(source.getBytes(StandardCharsets.UTF_8)));
    }
}
