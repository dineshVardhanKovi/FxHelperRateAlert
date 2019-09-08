package com.fxHelper.general.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.json.internal.Charsets;

public class AESCryptor {
	private static Logger log = LoggerFactory.getLogger(AESCryptor.class);
	private static final String ALGORITHM = "AES";
	SecretKeySpec keySpec;
	SecretKey key;
	Cipher cipher;
	byte[] encoded;
	
	//Base64 OurBase64 = new Base64();
	 
	public AESCryptor(String keyFileName) throws Exception {
		String keyFilePath = "./";
		keyFilePath = keyFilePath + File.separator + keyFileName;
		File keyFile = new File(keyFilePath);
		cipher = Cipher.getInstance(ALGORITHM);
		if (keyFile.exists()) {
			int len = (int) keyFile.length();
			encoded = new byte[len];
			FileInputStream fis = new FileInputStream(keyFile);
			int size = fis.read(encoded);
			keySpec = new SecretKeySpec(encoded, ALGORITHM);
		} else {
			throw new Exception("Invalid keyfile specified");
		}
	}

	public AESCryptor(File keyFile) throws Exception {
		log.debug("AESCryptor constructor/File");
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			log.debug("cipher implements " + cipher.getAlgorithm());
			log.debug("cipher provided by  " + cipher.getProvider().getName()
					+ "class is  " + cipher.getProvider().getClass().getName());
			if (keyFile.exists()) {
				int len = (int) keyFile.length();
				encoded = new byte[len];
				FileInputStream fis = new FileInputStream(keyFile);
				int size = fis.read(encoded);
				log.debug("READ key as bytes " + encoded);
				keySpec = new SecretKeySpec(encoded, ALGORITHM);
			} else {
				throw new Exception("Invalid keyfile specified");
			}
		} catch (Exception e) {
			log.error("exception in constructor", e);
		}
	}

	public AESCryptor(InputStream keyFileIS) throws Exception {
		log.debug("AESCryptor constructor/FileInputStream");
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			log.debug("cipher implements " + cipher.getAlgorithm());
			log.debug("cipher provided by  " + cipher.getProvider().getName()
					+ "class is  " + cipher.getProvider().getClass().getName());
			encoded = new byte[keyFileIS.available()];
			int size = keyFileIS.read(encoded);
			log.debug("READ key as bytes " + encoded);
			keySpec = new SecretKeySpec(encoded, ALGORITHM);
		} catch (Exception e) {
			throw new Exception("Invalid keyfile specified");
		}
	}

	public byte[] encrypt(String rawData) {
		byte[] encoded = "".getBytes();
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			if (rawData != null) {
				encoded = cipher.doFinal(rawData.getBytes());
				log.debug("encoded: " + encoded);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("exception in encrypt", e);
			} else if (log.isInfoEnabled()) {
				log.info("exception in encrypt");
			}
		}
		return encoded;
	}


	public byte[] encryptWithIV(String rawData, byte[] initVector) {
		byte[] encoded = "".getBytes();
		IvParameterSpec iv = new IvParameterSpec(initVector);
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
			if (rawData != null) {
				encoded = cipher.doFinal(rawData.getBytes());
				log.debug("encoded: " + encoded);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("exception in encrypt", e);
			} else if (log.isInfoEnabled()) {
				log.info("exception in encrypt");
			}
		}
		return encoded;
	}
	
	public synchronized byte[] decrypt(byte[] encData) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] original = cipher.doFinal(encData);
			log.debug("original: " + original);
			return original;
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("exception in decrypt", e);
			} else if (log.isInfoEnabled()) {
				log.info("exception in decrypt");
			}
		}
		return "".getBytes();
	}

	public String encryptBase64(String rawData) {
		String encString = new String();
		try {
			if (rawData != null) {
				encString = OurBase64.encode(encrypt(rawData));
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("exception in encryptBase64", e);
			} else if (log.isInfoEnabled()) {
				log.info("exception in encryptBase64");
			}
		}
		return encString;
	}

	public String encryptBase64WithIV(String rawData, byte[] initVector) {
		String encString = new String();
		try {
			if (rawData != null) {
				encString = OurBase64.encode(encryptWithIV(rawData, initVector));
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("exception in encryptBase64", e);
			} else if (log.isInfoEnabled()) {
				log.info("exception in encryptBase64");
			}
		}
		return encString;
	}
	
	public String decryptBase64(String encData) {
		String decString = new String();
		try {
			if (encData != null) {
				byte[] b64Bytes = OurBase64.sloppyDecode(encData);
				decString = new String(decrypt(b64Bytes), Charsets.UTF_8);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("exception in decryptBase64", e);
			} else if (log.isInfoEnabled()) {
				log.info("exception in decryptBase64");
			}
		}
		return decString;
	}
	
	public static void main(String args[]) throws Exception
	{
		//AESCryptor aesCryptor=new AESCryptor("cookieKey");
		//System.out.println("mednet cookie value: "+aesCryptor.encryptBase64("15483046T1497030541337_14970305413371497030541348"));
		/*InputStream  is=  PortFinder.class.getClassLoader()
	            .getResourceAsStream("cookieKey");
		AESCryptor aesCryptor = new AESCryptor(is);*/
		
		System.out.println(File.separator.toString());
		
	}
}
