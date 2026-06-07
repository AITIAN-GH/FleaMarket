package com.xu.system;

import org.springframework.util.DigestUtils;

public class test {
    public static void main(String[] args) {
        String psw = "123456";
        String s = DigestUtils.md5DigestAsHex(new StringBuilder(
                DigestUtils.md5DigestAsHex(psw.getBytes())).reverse().toString().getBytes());
        System.out.println("s = " + s);
    }
}
