package com.web.database;

import java.util.HashMap;
import java.util.Map;
import com.web.base.MysqlBase;
import com.web.util.Util;
import com.web.util.SqlHelper;

public class Admins extends MysqlBase {

	protected String adminTable = DATABASE + ".admin";
	protected String userTable = DATABASE + ".users";
	protected String newsTable = DATABASE + ".news";
	protected SqlHelper sqlHelper = new SqlHelper();
	
	

	/**
	 * 检查管理员名称是否存在
	 * 
	 * @param username
	 * @return true 表示不存在
	 */
	public boolean checkUserName(String username) {
		String sql = "SELECT COUNT(1) num FROM "+ adminTable +" WHERE `username` = ?";
		Map<String, Object> data = jdbcTPL.queryForMap(sql, new Object[] {username});
		if (Util.toInt(data.get("num")) > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 管理员信息插入
	 * 
	 * @param data Map
	 * @return
	 */
	public int insertAdmin(Map<String, String> data) {
		String sql = "INSERT INTO "+ adminTable +"(`username`, `password`, `email`, `addtime`) VALUES(?, ?, ?, ?)";
		try {
			return jdbcTPL.update(sql, new Object[] { data.get("username"), data.get("password"), data.get("email"), data.get("addtime") });
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 管理员列表
	 * @param data
	 * @return
	 */
	public HashMap<String, Object> listAdmin(HashMap<String, String> data) {
		String cols = "`adminid`, `username`, `email`, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime, IF(`edittime` > 0,FROM_UNIXTIME(`edittime`, '%Y-%m-%d %h:%i'), '-') edittime";
		sqlHelper.select(cols).from(adminTable);
		if (data.get("search") != null && !"".equals(data.get("search"))) {
			sqlHelper.like("`username`", "%"+ data.get("search") +"%").likeOr("`email`", "%"+ data.get("search") +"%");
		}
		sqlHelper.order("`adminid`", "DESC").limit(Util.toInt(data.get("page_num")), Util.toInt(data.get("page_size")));
		String querySql = sqlHelper.getSelectSql();
		String countSql = sqlHelper.getCountSql();
		sqlHelper.reset();
		Map<String, Object> count = jdbcTPL.queryForMap(countSql);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("rows", jdbcTPL.queryForList(querySql));
		result.put("count", count.get("num"));
		return result;
	}
	
	/**
	 * 删除
	 * @param adminid
	 * @return
	 */
	public int delete(String[] adminids) {
		if (adminids == null || adminids.length == 0) {
			return -1;
		}
		String sql = "DELETE FROM " + adminTable + " WHERE `adminid` IN("+ Util.join(adminids, ", ") +")";
		return jdbcTPL.update(sql);
	}
	
	/**
	 * 查询管理员
	 * @param adminid
	 * @return
	 */
	public Map<String, Object> getAdmin(int adminid) {
		String sql = sqlHelper.from(adminTable).where("`adminid`", adminid).getSelectSql();
		sqlHelper.reset();
		try {
			return jdbcTPL.queryForMap(sql);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 更新管理员信息
	 * @param data
	 * @param adminid
	 * @return
	 */
	public int updateAdmin(HashMap<String, String> data, int adminid) {
		if (adminid == 0) {
			return 0;
		}
		sqlHelper.reset();
		String sql = sqlHelper.update(adminTable, data).where("`adminid`", adminid).getUpdateSql();
		return jdbcTPL.update(sql);
	}
	
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @param ip
	 * @return
	 */
	public Map<String, Object> adminLogin(String username, String password, String ip) {
		if (Util.isEmpty(username) || Util.isEmpty(password)) {
			return null;
		}
		String sql = sqlHelper.select("adminid, username, email, menus").from(adminTable).where("`username`", username).where("`password`", password).getSelectSql();
		sqlHelper.reset();
		Map<String, Object> data = null;
		try {
			data = jdbcTPL.queryForMap(sql);
		} catch (Exception e) {
			
		}
		
		if (data != null && data.size() > 0) {
			sql = "UPDATE " + adminTable + " SET lastlogin = " + Util.getTime() + ", login_num = login_num + 1, lastip = '"+ ip +"' WHERE username = '"+ username +"'";
			jdbcTPL.update(sql);
		}
		return data;
	}
	
	/**
	 * 统计
	 * @return
	 */
	public Map<String, Object> count() {
		String sql = sqlHelper.from(this.userTable).getCountSql();
		sqlHelper.reset();
		Map<String, Object> data = jdbcTPL.queryForMap(sql);
		data.put("user", data.get("num"));
		data.remove("num");
		
		sql = sqlHelper.from(this.newsTable).getCountSql();
		sqlHelper.reset();
		data.put("news", jdbcTPL.queryForMap(sql).get("num"));
		
		sql = sqlHelper.select("SUM(click) num").from(this.newsTable).getSelectSql();
		sqlHelper.reset();
		data.put("click", jdbcTPL.queryForMap(sql).get("num"));
		
		sql = sqlHelper.select("uid, email, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime").from(this.userTable).order("uid", "DESC").limit(0, 10).getSelectSql();
		sqlHelper.reset();
		data.put("userRows", jdbcTPL.queryForList(sql));
		
		sql = sqlHelper.select("id, title, click").from(this.newsTable).order("click, id", "DESC").limit(0, 10).getSelectSql();
		sqlHelper.reset();
		data.put("newsRows", jdbcTPL.queryForList(sql));
		
		sql = sqlHelper.from(this.adminTable).getCountSql();
		sqlHelper.reset();
		data.put("admin", jdbcTPL.queryForMap(sql).get("num"));
		

		return data;
	}
	
	
}
