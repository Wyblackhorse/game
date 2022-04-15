package com.oxo.ball.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Util {
    public static String byteToBase64(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }
}
