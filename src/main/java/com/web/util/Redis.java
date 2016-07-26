/**
 * @ClassName:     Redis.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2016-1-14 上午10:08:19
 * @Description:   TODO
 *
 */

package com.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Redis {

	private static Logger logger = Logger.getLogger("user");
	private static JedisPool slavePool = null;
	private static JedisPool masterPool = null;
	private static JedisPool sessionPool = null;

	/**
	 * 从机
	 * @return
	 */
	public static JedisPool getSlavePool() {
		if (slavePool == null) {
			logger.info("slavePool is null");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(5);
			config.setMaxTotal(2400);
			slavePool = new JedisPool(config, Util.getConfig("redis.slave.ip"), Util.toInt(Util.getConfig("redis.slave.port")));
		}
		return slavePool;
	}
	
	/**
	 * 主机
	 * @return
	 */
	public static JedisPool getMasterPool() {
		if (masterPool == null) {
			logger.info("masterPool is null");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(5);
			config.setMaxTotal(2400);
			masterPool = new JedisPool(config, Util.getConfig("redis.master.ip"), Util.toInt(Util.getConfig("redis.master.port")));
		}
		return masterPool;
	}
	
	/**
	 * session服务器
	 * @return
	 */
	public static JedisPool getSessionPool() {
		if (sessionPool == null) {
			logger.info("sessionPool is null");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(5);
			config.setMaxTotal(2400);
			sessionPool = new JedisPool(config, Util.getConfig("redis.session.ip"), Util.toInt(Util.getConfig("redis.session.port")));
		}
		return sessionPool;
	}


	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String value = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getSlavePool();
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			if (jedis != null) {
				jedis.close();
			}
		}

		return value;
	}

	/**
	 * 写入数据 Master 服务器
	 * @param key
	 * @param value
	 * @return
	 */
	public static String set(String key, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getMasterPool();
			jedis = pool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return "";
	}
	
	/**
	 * 写入session,默认1个小时
	 * @param key
	 * @param value
	 * @return
	 */
	public static String setSession(String key, String value) {
		return Redis.setSession(key, value, Util.toInt(Util.getConfig("session.timeout")));
	}
	
	/**
	 * 写入session
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public static String setSession(String key, String value, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getSessionPool();
			jedis = pool.getResource();
			return jedis.setex(key, expire, value);
		} catch (Exception e) {
			// 释放redis对象
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return "";
	}
	
	/**
	 * 根据Key从Redis获取其内容
	 * @param key
	 * @return
	 */
	public static String getSession(String key) {
		String value = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getSessionPool();
			jedis = pool.getResource();
			value = jedis.get(key);
			if (value != null) {
				jedis.expire(key, Util.toInt(Util.getConfig("session.timeout")));
				return key;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return value;
	}
	
	/**
	 * 获取session内容, 从客户端cookie中解析key, 然后从Redis中获取
	 * @param request
	 * @return
	 */
	public static String getSession(HttpServletRequest request) {
		String cookie = Util.parseCookie(Util.getConfig("cookie.key"), request);
		System.out.println("getSession cookie: " + cookie);
		if ("".equals(cookie)) {
			return null;
		}
		String uid = Util.parseCookie(cookie);
		return Redis.getSession(uid);
	}


	
}
