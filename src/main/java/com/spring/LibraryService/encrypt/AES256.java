package com.spring.LibraryService.encrypt;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AES256 {
	
	public static String createKey(){ 
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(256);
			Key secretKey = generator.generateKey();
			return DatatypeConverter.printBase64Binary(secretKey.getEncoded());
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    public static String encrypt(String text, String key){
    	String iv = key.substring(0, 16);
    	try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
    	}catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    public static String decrypt(String cipherText, String key) {
    	String iv = key.substring(0, 16);
    	try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
            IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, "UTF-8");
    	}catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
}