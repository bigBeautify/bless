package com.example.myluck.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Arrays;

import com.example.myluck.contants.Constant;
import org.apache.commons.codec.digest.DigestUtils;



public class Md5Utils {
    /**
     * MD5方法
     *
     * @param text 明文
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text) throws Exception {
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text + Constant.MD5_KEY);
        System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param md5 密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String md5) throws Exception {
        //根据传入的密钥进行验证
        String md5Text = md5(text);
        if(md5Text.equalsIgnoreCase(md5))
        {
            System.out.println("MD5验证通过");
            return true;
        }

        return false;
    }

    //测试
    public static void main(String[] args) {
        try {
            System.out.println(Md5Utils.verify("123456",  Md5Utils.md5("123456")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
