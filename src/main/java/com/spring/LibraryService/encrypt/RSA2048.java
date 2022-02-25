package com.spring.LibraryService.encrypt;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSA2048{
	public static String keyToString(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	public static String encrypt(String plaintext, String publickey){
		try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] publickeyBytes = Base64.getDecoder().decode(publickey.getBytes());
            X509EncodedKeySpec publickeySpec = new X509EncodedKeySpec(publickeyBytes);
            Key key = keyFactory.generatePublic(publickeySpec);
            
		    byte[] plaintextBytes = plaintext.getBytes();
		    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.ENCRYPT_MODE,key);
		    byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);
		    return encode(ciphertextBytes);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String decrypt(String ciphertext, String privatekey){
	    try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] privatekeyBytes = Base64.getDecoder().decode(privatekey.getBytes());
            PKCS8EncodedKeySpec privatekeySpec = new PKCS8EncodedKeySpec(privatekeyBytes);
            Key key = keyFactory.generatePrivate(privatekeySpec);
            
		    byte[] ciphertextBytes = decode(ciphertext);
		    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    cipher.init(Cipher.DECRYPT_MODE,key);
		    byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
		    return new String(plaintextBytes,"UTF8");
	    }catch(Exception e) {
			e.printStackTrace();
			return null;
	    }
	}
	private static String encode(byte[] data){
	    return Base64.getEncoder().encodeToString(data);
	}
	private static byte[] decode(String data){
	    return Base64.getDecoder().decode(data);
	}
	public static KeyPair createKey() {
		KeyPairGenerator gen;
		KeyPair keypair = null;
		try {
			gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(2048);
			keypair = gen.genKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keypair;
	}
}