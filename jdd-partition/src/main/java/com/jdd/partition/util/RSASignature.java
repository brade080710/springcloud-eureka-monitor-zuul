package com.jdd.partition.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RSA签名验签类
 */
public class RSASignature {
	private final static Logger logger = LoggerFactory.getLogger(RSASignature.class);

	/**
	 * 签名算法
	 */
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * RSA签名
	 * 
	 * @param content    待签名数据
	 * @param privateKey 商户私钥
	 * @param encode     字符集编码
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String encode) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));

			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(encode));

			byte[] signed = signature.sign();
			return Base64.getEncoder().encodeToString(signed);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initSign(priKey);
			signature.update(content.getBytes());
			byte[] signed = signature.sign();
			return Base64.getEncoder().encodeToString(signed);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * RSA验签名检查
	 * 
	 * @param content   待签名数据
	 * @param sign      签名值
	 * @param publicKey 分配给开发商公钥
	 * @param encode    字符集编码
	 * @return 布尔值
	 */
	public static boolean doCheck(String content, String sign, String publicKey, String encode) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.getDecoder().decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(encode));

			boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean doCheck(String content, String sign, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.getDecoder().decode(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

		signature.initVerify(pubKey);
		signature.update(content.getBytes());

		boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
		return bverify;
	}


	public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
//		String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIrRLDgUd2KN4+4CpP+B2/iYGMG8Rroo1kQSlxHvi0kXI1bR12jo2xEgIsEj4oq5MFJNWoVX6c6q35jjNgkcNdRTIuUbXXokrbhiT98usV7ofzB9sdcG5k+G8d4pYEy0Y9bv6uixfkIWO0Gcyi+4nF5zsDaKa7nRzWLxrufaqtJpAgMBAAECgYArN3Gvx9dfDFOAfrxFe3UzEI7siaPvQtVp/eugPtY7HAP5fHy2gX/p9fo77OMTUxITHVfoGXWFzLatz1a8QVcUXDM0I/9N5nGBCmpviQC+w/j/E08hpus8BAh9lRb8vMoMgLgx3Gb1Tc9rmIOiXtiZIVo28F28eponBPPgwdlDvQJBAM3y8eODfut5gTtLvrN+6SM4xT6Zbbn5l0mqiU9HO+28zwQkVRp5gxzc+9MkDX2zzzR7mnD7RrWp+Cq8VB27+u8CQQCsjaKzT2L0QI5H7xX8fgJtj45JcbXGXsY54nHHSFU5P50MDQJS48AoDBVhqupm1iP+iHV4zUaM95g1iiaOUegnAkBj+Zmevkiweo1XgY+ThvoZ1Qc7OjPf4d+lLRn22DepUsyT0CdX8MDLFCNS/d9mtBDUA2SXkANlUJtd84pJ3OUVAkBCIYIykIFPNRKSlRnrC+woSzVwBmlxHk+ATb3nb1xiNXQd8zppxCmX75GePd/aC8X9vhSveJTKX/XwxwKh1thPAkBlvz74WAzgI0LwSiiIPM00m7xfdOjpUTRIZrXx1SCrBiZFXGkFIKBaGAAnoqQ8PcZOCfE5zyeUxGklyc27O61R";
//		String publicKey= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCK0Sw4FHdijePuAqT/gdv4mBjBvEa6KNZEEpcR74tJFyNW0ddo6NsRICLBI+KKuTBSTVqFV+nOqt+Y4zYJHDXUUyLlG116JK24Yk/fLrFe6H8wfbHXBuZPhvHeKWBMtGPW7+rosX5CFjtBnMovuJxec7A2imu50c1i8a7n2qrSaQIDAQAB";
//		String content = "/appUser/loginBySmscode?LOGIN_TEL=15138692055&RANDOM=968512&clientid=865810036043277&timestamp=2019-04-25 14:10:29&version=3.0.0";
//		System.out.println(RSASignature.sign(content,priKey));
//		System.out.println(RSASignature.doCheck(content, "3826e201d6810c1866011a7fca033874d9183a1beef19959664bdbcacefcb98d\nfa1a90177bb3a6e2bff3614111c504bf129c9ee1eb882cf15e14b8f31b2d928c\n62c1154ef8954daeb814b872ceec559e2b5c1e8b1592c3e618da9be45c48d26a\nc7228621cf89fb4c44a01a12b59a34a12431e0b27a4a7593e7fb7eb9869e697e".replace('\n','+'),publicKey));
	}

}