/**
 * @ClassName:     Users.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-18 下午1:19:28
 * @Description:   TODO
 *
 */

package com.web.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.base.MysqlBase;
import com.web.util.SqlHelper;
import com.web.util.Util;

public class Users extends MysqlBase {

	protected String userTable = DATABASE + ".users";
	protected SqlHelper sqlHelper = new SqlHelper();

	/**
	 * 用户列表
	 * 
	 * @param data
	 * @return
	 */
	public HashMap<String, Object> list(HashMap<String, String> data) {
		String cols = "uid, email, last_ip, login_num, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime, IF(`edittime` > 0,FROM_UNIXTIME(`last_login`, '%Y-%m-%d %h:%i'), '-') last_login";
		sqlHelper.select(cols).from(userTable);
		if (!Util.isEmpty(data.get("search"))) {
			sqlHelper.like("`email`", "%" + data.get("search") + "%");
		}

		sqlHelper.order("`uid`", "DESC").limit(Util.toInt(data.get("page_num")), Util.toInt(data.get("page_size")));
		String querySql = sqlHelper.getSelectSql();
		String countSql = sqlHelper.getCountSql();
		sqlHelper.reset();

		HashMap<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> count = jdbcTPL.queryForMap(countSql);
		result.put("rows", jdbcTPL.queryForList(querySql));
		result.put("count", count.get("num"));
		return result;
	}

	/**
	 * 最新注册用户
	 * 
	 * @param data
	 * @return
	 */
	public List<Map<String, Object>> getList(HashMap<String, String> data) {
		String cols = "uid, email, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime";
		sqlHelper.select(cols).from(userTable);
		sqlHelper.order("`uid`", "DESC").limit(Util.toInt(data.get("page_num")), Util.toInt(data.get("page_size")));
		String querySql = sqlHelper.getSelectSql();
		sqlHelper.reset();
		return jdbcTPL.queryForList(querySql);
	}

	/**
	 * 用户注册
	 * 
	 * @param data
	 * @return
	 */
	public int insert(HashMap<String, String> data) {
		String sql = sqlHelper.getInsertSql(userTable, data);
		if (jdbcTPL.update(sql) > 0) {
			sql = sqlHelper.select("uid").from(userTable).where("email", data.get("email")).where("password", data.get("password")).getSelectSql();
			sqlHelper.reset();
			Map<String, Object> row = jdbcTPL.queryForMap(sql);
			return Util.toInt(row.get("uid"));
		}
		return 0;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param uid
	 * @return
	 */
	public Map<String, Object> getUser(String username) {
		String cols = "*, FROM_UNIXTIME(`add_time`, '%Y-%m-%d %h:%i') adddate, FROM_UNIXTIME(`modify_time`, '%Y-%m-%d %h:%i') editdate";
		String sql = sqlHelper.select(cols).from(Util.getTable(username)).where("username", username).getSelectSql();
		sqlHelper.reset();
		return jdbcTPL.queryForMap(sql);
	}

	/**
	 * 检查Email是否存在
	 * 
	 * @param email
	 * @return 存在 返回true
	 */
	public boolean checkEmail(String email) {
		boolean flag = false;
		String sql = sqlHelper.from(userTable).where("email", email).getCountSql();
		sqlHelper.reset();
		Map<String, Object> row = jdbcTPL.queryForMap(sql);
		if (Util.toInt(row.get("num")) > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 用户登录
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public Map<String, Object> login(String email, String password) {
		String sql = sqlHelper.select("uid, email, addtime, login_num").from(userTable).where("email", email).where("`password`", password).getSelectSql();
		sqlHelper.reset();
		try {
			return jdbcTPL.queryForMap(sql);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 更新用户信息
	 * 
	 * @param uid
	 * @param data
	 * @return
	 */
	public int update(int uid, HashMap<String, String> data) {
		String sql = sqlHelper.update(userTable, data).where("uid", uid).getUpdateSql();
		return jdbcTPL.update(sql);
	}
}
