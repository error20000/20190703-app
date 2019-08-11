package com.jian.system.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Utils {

	public static boolean isNullOrEmpty(Object str){
		if(str == null || "".equals(str)){
			return true;
		}else{
			return false;
		}
	}

	public static String newUUId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String newUUIdMd5() {
		return md5(UUID.randomUUID().toString());
	}
	
	public static String newId(String suffix) {
		int random = new Random().nextInt(10000);
		String str = random+"";
		for (int i = 0; i <  4 - str.length(); i++) {
			str = "0" + str;
		}
		return suffix + DateTools.formatDate("yyyyMMddHHmmssSSS") + str;
	}

	public static long newSnowflakeId() {
		return SnowflakeIdWorker.generateId();
	}

	public static String newSnowflakeIdStr() {
		return String.valueOf(SnowflakeIdWorker.generateId());
	}


	/**
	 * MD5，默认字符编码 “utf-8”
	 * @param str 待加密字符串
	 * @return String 返回md5字符串
	 */
	public static String md5(String str) {
		return md5(str, "utf-8");
	}

	/**
	 * MD5
	 * @param str  待加密字符串
	 * @param charsetName  编码，默认 “utf-8”
	 * @return String 返回md5字符串
	 */
	public static String md5(String str, String charsetName) {
		try {
			charsetName = isNullOrEmpty(charsetName) ? "utf-8" : charsetName;
			return md5(str.getBytes(charsetName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * MD5
	 * @param bytes  待加密字节数组
	 * @return String 返回md5字符串
	 */
	public static String md5(byte[] bytes) {
		String res = "";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(bytes);
			res = getFormattedText(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 转换成十六进制的字符串形式
	 * @param bytes 字节数组
	 * @return String 返回十六进制的字符串
	 */
	public static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuffer buf = new StringBuffer();
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			String shaHex = Integer.toHexString(bytes[j] & 0xFF);
			if (shaHex.length() < 2) {
				buf.append(0);
			}
			buf.append(shaHex);

		}
		return buf.toString();
	}

	public static byte[] compress(byte[] buff) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzos = new GZIPOutputStream(baos);
		gzos.write(buff);
		gzos.close();
		baos.close();
		return baos.toByteArray();
	}
	
	public static byte[] uncompress(byte[] buff) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(buff);
		GZIPInputStream gzis = new GZIPInputStream(bais);
		byte[] temp = new byte[1024];
		int len = 0;
		while ((len = gzis.read(temp)) >= 0) {
			out.write(temp, 0, len);
		}
		byte[] receive = out.toByteArray();
		gzis.close();
		bais.close();
		return receive;
	}
	
	public static void main(String[] args) throws IOException {
		byte[] b  = new byte[1000];
		for (int i = 0;i < b.length;++i) {
			b[i] = 65;
		}
		byte[] buff = compress(b);
		System.out.println(buff.length);
		
		byte[] c = uncompress(buff);
		System.out.println(c.length);
	}
}
