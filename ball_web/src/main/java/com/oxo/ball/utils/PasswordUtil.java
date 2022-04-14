package com.oxo.ball.utils;

import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotNull;

/**
 * @author flooming
 */
public class PasswordUtil {
    private static final String PWD_SALT = "#COCO_";

    public static String genPasswordMd5(@NotNull String pwd) {
        final String salted = PWD_SALT + pwd;
        final String result = DigestUtils.md5DigestAsHex(salted.getBytes());
        return result;
    }
    public static String genMd5(@NotNull String pwd) {
        final String result = DigestUtils.md5DigestAsHex(pwd.getBytes());
        return result;
    }

    public static void main(String[] args) {
        System.out.println(genPasswordMd5("123123"));
    }
}
