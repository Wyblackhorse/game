package com.oxo.ball.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class UUIDUtil {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static void main(String[] args) {
//        System.out.println(getUUID());

        Set<String> source = new HashSet<>();
        source.add("1");
        source.add("2");
        source.add("3");
        source.add("4");
        source.add("5");
        source.add("5");
        source.add("5");


        Set<String> copy = new HashSet<>();
        for (Iterator<String> it = source.iterator(); it.hasNext(); ) {
            String c = it.next();
            copy.add(c);
        }
        System.out.println(source.size());
        source.clear();
        System.out.println(source);
        System.out.println(copy);
    }

    public static String urlDecode(String string){
        try {
            return URLDecoder.decode(string,"UTF8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }
}
