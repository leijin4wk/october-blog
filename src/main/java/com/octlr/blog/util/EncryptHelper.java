package com.octlr.blog.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

public class EncryptHelper {
    public static String aesEncryptWithIv(String str, String keyBytes, String ivBytes) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivBytes.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); ////"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);    // 确定密钥
            byte[] result = cipher.doFinal(str.getBytes());  // 加密
            return Base64.encodeBase64String(result);  // 不进行Base64编码的话，那么这个字节数组对应的字符串就是乱码
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String aesEncrypt(String str, String keyBytes) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING"); // //"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, key);    // 确定密钥
            byte[] result = cipher.doFinal(str.getBytes());  // 加密
            return bytesToHex(result) ;  // 不进行Base64编码的话，那么这个字节数组对应的字符串就是乱码
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static String rsaEncrypt(String str, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] bytes = new byte[128 - str.length()];
            Arrays.fill(bytes, (byte) 0);
            byte[] result = cipher.doFinal(byteMerger(bytes, str.getBytes()));  // 加密
            return bytesToHex(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
