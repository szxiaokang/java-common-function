package com.web.base;

import java.sql.Connection;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.web.util.Util;

public class MysqlBase {

	public String DATABASE = Util.getConfig("mysql.database");
	protected static BoneCPDataSource bonecpDataSource;
	protected JdbcTemplate jdbcTPL = null;
	private static int retryNum = 3;
	private static Logger logger = Logger.getLogger("sys");
	
	
	
	public MysqlBase() {
		if (jdbcTPL == null && bonecpDataSource == null) {
			try {
				
				BoneCPConfig cfg = new BoneCPConfig();
				Configuration config = new PropertiesConfiguration(Util.configFile);
				Class.forName(config.getString("mysql.driverClass"));
				cfg.setJdbcUrl(config.getString("mysql.jdbcUrl"));
				cfg.setUsername(config.getString("mysql.username"));
				cfg.setPassword(config.getString("mysql.password"));
				cfg.setMinConnectionsPerPartition(Integer.parseInt(config.getString("mysql.minConnectionsPerPartition")));
				cfg.setMaxConnectionsPerPartition(Integer.parseInt(config.getString("mysql.maxConnectionsPerPartition")));
				cfg.setPartitionCount(Integer.parseInt(config.getString("mysql.partitionCount")));
				try {
					bonecpDataSource = new BoneCPDataSource(cfg);
					jdbcTPL = new JdbcTemplate(bonecpDataSource);
					logger.info("connection successed");
				} catch (Exception e) {
					while (retryNum > 0) {
						logger.info("Retry " + retryNum);
						retryNum--;
						MysqlBase x = new MysqlBase();
						if (jdbcTPL != null) {
							break;
						}
					}
					
					
				}
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				logger.fatal("connection failed: ConfigurationException, " + e.getMessage());
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				logger.fatal("connection failed: ClassNotFoundException, " + e.getMessage());
				e.printStackTrace();
			}
		} else if (jdbcTPL == null && bonecpDataSource != null) {
			jdbcTPL = new JdbcTemplate(bonecpDataSource);
		}
	}

	/**
	 * close connection
	 * 
	 * @param conn
	 * @throws Exception
	 */
	public void close(Connection conn) throws Exception
	{
		if (conn != null) {
			conn.close();
		}
	}
}
