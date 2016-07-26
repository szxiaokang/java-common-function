/**
 * @ClassName:     Util.java
 * @author         KangYun
 * @version        V1.0 
 * @Date           2013-11-20 下午8:15:39
 * @Description:   常用方法
 *
 */

package com.web.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONObject;

public class Util {

	private static final String HMAC_MD5 = "HmacMD5";
	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String _BR = "<br/>";
	public static String configFile = "config.properties";

	/**
	 * MD5 加密
	 * 
	 * @param src
	 * @return
	 */
	public static String MD5(String src) {
		if (src == null || "".equals(src)) {
			return null;
		}
		try {
			byte[] btInput = src.getBytes();
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
	 * 加密
	 * 
	 * @param src
	 * @param key
	 * @return
	 */
	public static String encryptHmac(String src, String key) {
		try {
			Mac hmac_md5 = Mac.getInstance(HMAC_MD5);
			hmac_md5.init(new SecretKeySpec(key.getBytes("utf-8"), HMAC_MD5));
			byte[] b = hmac_md5.doFinal(src.getBytes("utf-8"));
			int j = b.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = b[i];
				str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
				str[(k++)] = hexDigits[(byte0 & 0xF)];
			}

			return new String(str);
		} catch (Exception ex) {
		}
		return "WRONG_EXCEPTION";
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getDate() {
		Date now = new Date();// 获取系统当前时间
		SimpleDateFormat s = new SimpleDateFormat(dateFormat);// 格式化日期，和数据库中日期格式相同，这里是datetime
		return s.format(now);
	}

	/**
	 * 获取当前日期, 根据指定格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getDate(String format) {
		Date now = new Date();// 获取系统当前时间
		SimpleDateFormat s = new SimpleDateFormat(format);// 格式化日期，和数据库中日期格式相同，这里是datetime
		return s.format(now);
	}

	/**
	 * 根据时间戳转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getDate(Long time) {
		if (time.toString().length() == 10) {
			time = time * 1000;
		}
		SimpleDateFormat s = new SimpleDateFormat(dateFormat);
		return s.format(new Date(time));

	}

	/**
	 * 获取当前时间戳，秒数
	 * 
	 * @return long
	 */
	public static Long getTime() {
		Long time = System.currentTimeMillis();
		return Long.parseLong(time.toString().substring(0, time.toString().length() - 3));
	}

	/**
	 * 返回当前时间毫秒数
	 * 
	 * @param microtime true
	 * @return
	 */
	public static Long getTime(boolean microtime) {
		return System.currentTimeMillis();
	}

	/**
	 * 根据日期转换成时间戳
	 * 
	 * @param date
	 * @return
	 */
	public static Long getTime(String date) {
		SimpleDateFormat s = new SimpleDateFormat(dateFormat);// 格式化日期，和数据库中日期格式相同，这里是datetime
		Date d;
		try {
			d = s.parse(date);
			Long time = d.getTime();
			return Long.parseLong(time.toString().substring(0, time.toString().length() - 3));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (long) 0;
	}

	/**
	 * 分割字符串
	 * 
	 * @param str String 原始字符串
	 * @param splitsign String 分隔符
	 * @return String[] 分割后的字符串数组
	 */
	public static String[] split(String str, String splitsign) {
		int index;
		if (str == null || splitsign == null) {
			return null;
		}
		ArrayList al = new ArrayList();
		while ((index = str.indexOf(splitsign)) != -1) {
			al.add(str.substring(0, index));
			str = str.substring(index + splitsign.length());
		}
		al.add(str);
		return (String[]) al.toArray(new String[0]);
	}

	/**
	 * 拼接字符串数组
	 * 
	 * @param data
	 * @param tag
	 * @return
	 */
	public static String join(String[] data, String tag) {
		int len = data.length;
		if (len > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < len; i++) {
				sb.append(data[i]);
				if (i + 1 < len) {
					sb.append(tag);
				}
			}
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 是否为空白,包括null和""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 判断输入的字符串是否符合Email样式.
	 * 
	 * @param str 传入的字符串
	 * @return 是Email样式返回true,否则返回false
	 */
	public static boolean isEmail(String str) {
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断输入的字符串是否为纯汉字
	 * 
	 * @param str 传入的字符窜
	 * @return 如果是纯汉字返回true,否则返回false
	 */
	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}


	/**
	 * 全角字符转半角字符
	 * 
	 * @param QJStr
	 * @return String
	 */
	public static final String QJToBJChange(String QJStr) {
		char[] chr = QJStr.toCharArray();
		String str = "";
		for (int i = 0; i < chr.length; i++) {
			chr[i] = (char) ((int) chr[i] - 65248);
			str += chr[i];
		}
		return str;
	}

	/**
	 * 过滤特殊字符
	 * 
	 * @param src
	 * @return
	 */
	public static String encoding(String src) {
		if (src == null)
			return "";
		StringBuilder result = new StringBuilder();
		if (src != null) {
			src = src.trim();
			for (int pos = 0; pos < src.length(); pos++) {
				switch (src.charAt(pos)) {
				case '\"':
					result.append("&quot;");
					break;
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '\'':
					result.append("&apos;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '%':
					result.append("&pc;");
					break;
				case '_':
					result.append("&ul;");
					break;
				case '#':
					result.append("&shap;");
					break;
				case '?':
					result.append("&ques;");
					break;
				default:
					result.append(src.charAt(pos));
					break;
				}
			}
		}
		return result.toString();
	}

	/**
	 * 反过滤特殊字符
	 * 
	 * @param src
	 * @return
	 */
	public static String decoding(String src) {
		if (src == null) {
			return "";
		}
		String result = src;
		result = result.replace("&quot;", "\"").replace("&apos;", "\'");
		result = result.replace("&lt;", "<").replace("&gt;", ">");
		result = result.replace("&amp;", "&");
		result = result.replace("&pc;", "%").replace("&ul", "_");
		result = result.replace("&shap;", "#").replace("&ques", "?");
		return result;
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取配置信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getConfig(String key) {
		Configuration config;
		try {
			config = new PropertiesConfiguration(configFile);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return config.getString(key);
	}

	/**
	 * 加密
	 * 
	 * @param content 需要加密的内容
	 * @param password 加密密码
	 * @return
	 */
	public static String encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return parseByte2HexStr(result); // 加密
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param content 待解密内容
	 * @param password 解密密钥
	 * @return
	 */
	public static String decrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(parseHexStr2Byte(content));
			return new String(result); // 加密
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * get random by between min and max
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandom(int min, int max) {
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public static int toInt(Object object) {
		if (object == null) {
			return 0;
		}
		return Integer.valueOf(String.valueOf(object));
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static int toInt(String s) {
		if (s == null) {
			return -1;
		}
		int i = -1;
		try {
			i = Integer.valueOf(s);
		} catch (Exception e) {

		}
		return i;
	}

	/**
	 * get current url
	 * 
	 * @param request
	 * @return
	 */
	public static String getURL(HttpServletRequest request) {
		String url = request.getRequestURL() + "";
		if (request.getQueryString() != null) {
			url += "?" + request.getQueryString();
		}
		try {
			byte[] s = Base64.encodeBase64URLSafe(url.getBytes("UTF-8"));
			return new String(s);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * base64 URL 编码
	 * 
	 * @param url
	 * @return
	 */
	public static String encodeBase64URL(String url) {
		String str = null;
		try {
			byte[] s = Base64.encodeBase64URLSafe(url.getBytes("UTF-8"));
			str = new String(s);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * base64 URL 解码
	 * 
	 * @param url
	 * @return
	 */
	public static String decodeBase64URL(String url) {
		String str = null;
		try {
			byte[] s = Base64.decodeBase64(url.getBytes("UTF-8"));
			str = new String(s);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * set cookie
	 * 
	 * @param 主信息
	 * @param response
	 * @return
	 */
	public static String setCookie(String domainName, String mainContent, HttpServletResponse response) {
		String cryptString = Util.getTime() + "|" + mainContent + "|" + Math.random();
		String encrypt = Util.encrypt(cryptString, Util.getConfig("cookie.encrypt.key"));
		Cookie cookie = new Cookie(domainName, encrypt);
		cookie.setDomain(Util.getConfig("cookie.domain"));
		cookie.setMaxAge(Integer.parseInt(Util.getConfig("cookie.timeout")));
		cookie.setPath(Util.getConfig("cookie.path"));
		response.addCookie(cookie);
		return encrypt;
	}

	/**
	 * set session to Memcache
	 * 
	 * @param session
	 * @return boolean
	 */
	public static boolean setSession(String key, Map<String, Object> session) {
		int expire = Integer.parseInt(Util.getConfig("session.timeout"));
		return Memcache.getInstance().set(key, expire, JSONObject.valueToString(session)) != null;
	}

	/**
	 * get session information by Key from Memcache
	 * 
	 * @param key
	 * @return Map
	 */
	public static Map<String, Object> getSession(String key) {
		if ("".equals(key)) {
			return null;
		}
		Object value = Memcache.getInstance().get(key);
		if (value != null) {
			Map<String, Object> result = new HashMap<String, Object>();
			JSONObject json = new JSONObject(value.toString());
			Iterator<String> it = json.keys();
			while (it.hasNext()) {
				String k = (String) it.next();
				Object v = json.get(k);
				result.put(k, v);
			}
			// update expires
			Util.setSession(key.toString(), result);
			return result;
		}
		return null;
	}

	/**
	 * get session by client cookie
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getSession(String cookieName, HttpServletRequest request) {
		String cookie = Util.parseCookie(cookieName, request);
		if ("".equals(cookie)) {
			return null;
		}
		String profileid = Util.parseCookie(cookie);
		return Util.getSession(profileid);
	}


	/**
	 * remove memcached value by key
	 * 
	 * @param key
	 */
	public static void removeSession(String key) {
		if (key != null && !"".equals(key)) {
			Memcache.getInstance().delete(key);
		}
	}

	/**
	 * remove cookie
	 * 
	 * @param response
	 */
	public static void removeCookie(String cookieName, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setDomain(Util.getConfig("cookie.domain"));
		cookie.setMaxAge(0);
		cookie.setPath(Util.getConfig("cookie.path"));
		response.addCookie(cookie);
	}

	/**
	 * parse cookie, get profileid
	 * 
	 * @param cookieEncrypt
	 * @return
	 */
	public static String parseCookie(String cookieEncrypt) {
		String result = Util.decrypt(cookieEncrypt, Util.getConfig("cookie.encrypt.key"));
		if (result == null) {
			return "";
		}
		String[] data = Util.split(result, "|");
		return data[1];
	}

	/**
	 * read cookie, get uid
	 * 
	 * @param request
	 * @return
	 */
	public static String parseCookie(String cookieName, HttpServletRequest request) {
		String value = "";
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return "";
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie c = cookies[i];
			if (c.getName().equalsIgnoreCase(cookieName)) {
				value = c.getValue();
				break;
			}
		}

		return value;
	}
	
	/**
	 * 根据用户名获取表名
	 * @param username
	 * @return
	 */
	public static String getTable(String username) {
		if (Util.isEmpty(username)) {
			return null;
		}
		String table = username.substring(0, 1);
		return "`" + Util.getConfig("database.account") + "`.`" + table + "`";
	}

}
