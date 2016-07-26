package com.web.util;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;

import org.apache.log4j.Logger;

public class Memcache {

	public static MemcachedClient instance = null;
	private static Logger logger = Logger.getLogger("sys");

	private Memcache() {
	}

	public static MemcachedClient getInstance() {
		if (instance == null) {
			try {
				// instance = new MemcachedClient(new BinaryConnectionFactory(),
				// AddrUtil.getAddresses(Util.getConfig("memcached.host") + ":"
				// + Util.getConfig("memcached.port")));
				instance = new MemcachedClient(new InetSocketAddress(Util.getConfig("memcached.host"), Util.toInt(Util.getConfig("memcached.port"))));
				logger.info("memcached init successed");
			} catch (NumberFormatException e) {
				logger.fatal("memcached NumberFormatException: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				logger.fatal("memcached IOException: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		return instance;
	}
}
