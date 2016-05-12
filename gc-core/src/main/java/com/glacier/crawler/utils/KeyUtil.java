package com.glacier.crawler.utils;

import java.util.Random;

/**
 * Created by Glacier on 16/5/10.
 */
public class KeyUtil {

    public static final String Characters = "123456789abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
    public static final String MagicNum = "do1_";
    public static final Integer KeySize = 20;

    public String generateKey() {
        Random random = new Random(System.currentTimeMillis());
        int len = Characters.length();
        char[] array = Characters.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < KeySize; i ++) {
            int index = random.nextInt(len);
            buffer.append(array[index]);
        }
        String key = MagicNum + buffer.toString();
        return key;
    }

}
