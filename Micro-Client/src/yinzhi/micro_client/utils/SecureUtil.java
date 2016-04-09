package yinzhi.micro_client.utils;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.lidroid.xutils.util.LogUtils;

import android.util.Base64;

public class SecureUtil {

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * SHA 加密
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encrypt_SHA(String source) throws Exception {
		MessageDigest sha = MessageDigest.getInstance("SHA");
		sha.update(source.getBytes());
		return Base64.encodeToString(sha.digest(), Base64.DEFAULT);

	}

	/**
	 * 生成mackey
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5");
		SecretKey secretKey = keyGenerator.generateKey();
		return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);

	}

	/**
	 * Hmac加密
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt_HMAC(String source, String key)
			throws Exception {
		SecretKey secretKey = new SecretKeySpec(Base64.decode(key,
				Base64.DEFAULT), "HmacMD5");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return Base64.encodeToString(mac.doFinal(source.getBytes()),
				Base64.DEFAULT);
	}

	/**
	 * AES加密
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt_AES(String source, String key)
			throws Exception {

		if (key == null) {
			LogUtils.e("key cant be null");
			return null;
		}
		if (key.length() != 16) {
			LogUtils.e("key's length is not 16");
			return null;
		}

		byte[] raw = key.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		IvParameterSpec iv = new IvParameterSpec(
				"102954873628943756".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
		byte[] encrypted = cipher.doFinal(source.getBytes());

		return Base64.encodeToString(encrypted, Base64.DEFAULT);

	}

	/**
	 * AES解密
	 * 
	 * @param source
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt_AES(String source, String key)
			throws Exception {
		if (key == null) {
			LogUtils.e("key cant be null");
			return null;
		}
		if (key.length() != 16) {
			LogUtils.e("key's length is not 16");
			return null;
		}
		byte[] raw = key.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec(
				"102954873628943756".getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
		byte[] encrypted = Base64.decode(source, Base64.DEFAULT);

		try {
			byte[] original = cipher.doFinal(encrypted);
			String originalString = new String(original);
			return originalString;
		} catch (Exception e) {
			return null;
		}

	}

}