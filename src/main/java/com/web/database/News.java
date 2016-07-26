/**
 * @ClassName:     News.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-11 下午1:36:20
 * @Description:   新闻相关DB操作
 *
 */


package com.web.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.base.MysqlBase;
import com.web.util.SqlHelper;
import com.web.util.Util;

public class News extends MysqlBase {
	
	protected String newsTable = DATABASE + ".news";
	protected String newsTypeTable = DATABASE + ".news_type";
	protected SqlHelper sqlHelper = new SqlHelper();
	
	/**
	 * 检测名称是否存在
	 * @param name
	 * @return
	 */
	public boolean checkName(String name) {
		return this.checkName(name, 0);
	}
	
	/**
	 * 检查用户名是否存在
	 * @param name
	 * @param id 0为不检查此名称的id
	 * @return
	 */
	public boolean checkName(String name, int id) {
		sqlHelper.select("COUNT(1) num").from(newsTypeTable).where("name", name);
		if (id != 0) {
			sqlHelper.where("id !=", id);
		}
		String sql = sqlHelper.getSelectSql();
		sqlHelper.reset();
		HashMap<String, Object> row = (HashMap<String, Object>) jdbcTPL.queryForMap(sql);
		if (Util.toInt(row.get("num")) > 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 插入类型
	 * @param data
	 * @return
	 */
	public int insertType(HashMap<String, String> data) {
		String sql = sqlHelper.getInsertSql(newsTypeTable, data);
		return jdbcTPL.update(sql);
	}
	
	/**
	 * 插入新闻
	 * @param data
	 * @return
	 */
	public int insert(HashMap<String, String> data) {
		String sql = sqlHelper.getInsertSql(newsTable, data);
		return jdbcTPL.update(sql);
	}
	
	/**
	 * 更新类型
	 * @param data
	 * @param id
	 * @return
	 */
	public int updateType(HashMap<String, String> data, int id) {
		String sql = sqlHelper.update(newsTypeTable, data).where("id", id).getUpdateSql();
		return jdbcTPL.update(sql);
	}
	
	/**
	 * 更新新闻
	 * @param data
	 * @param id
	 * @return
	 */
	public int update(HashMap<String, String> data, int id) {
		String sql = sqlHelper.update(newsTable, data).where("id", id).getUpdateSql();
		return jdbcTPL.update(sql);
	}
	
	/**
	 * 获取类型数据
	 * @param id
	 * @return
	 */
	public Map<String, Object> getType(int id) {
		String sql = sqlHelper.from(newsTypeTable).where("id", id).getSelectSql();
		sqlHelper.reset();
		try {
			return jdbcTPL.queryForMap(sql);
		} catch (Exception e) {
			return null;
		}
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getNews(int id) {
		String sql = sqlHelper.select("*, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') adddate").from(newsTable).where("id", id).getSelectSql();
		sqlHelper.reset();
		try {
			return jdbcTPL.queryForMap(sql);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 删除类型
	 * @param ids
	 * @return
	 */
	public int deleteType(String[] ids) {
		String sql = sqlHelper.from(newsTypeTable).whereIn("id", ids).getDeleteSql();
		String sql2 = sqlHelper.from(newsTable).whereIn("type", ids).getDeleteSql();
		return jdbcTPL.update(sql) != -1 && jdbcTPL.update(sql2) != -1 ? 1 : -1;
	}

	/**
	 * 删除新闻
	 * @param ids
	 * @return
	 */
	public int delete(String[] ids) {
		String sql = sqlHelper.from(newsTable).whereIn("id", ids).getDeleteSql();
		return jdbcTPL.update(sql);
	}
	
	/**
	 * 类型列表
	 * @param data
	 * @return
	 */
	public HashMap<String, Object> listType(HashMap<String, String> data) {
		String cols = "`id`, `name`, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime, IF(`edittime` > 0,FROM_UNIXTIME(`edittime`, '%Y-%m-%d %h:%i'), '-') edittime";
		sqlHelper.select(cols).from(newsTypeTable);
		if (data.get("search") != null && !"".equals(data.get("search"))) {
			sqlHelper.like("`name`", "%"+ data.get("search") +"%");
		}
		sqlHelper.order("`id`", "DESC").limit(Util.toInt(data.get("page_num")), Util.toInt(data.get("page_size")));
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
	 * 获取分类列表
	 * @return
	 */
	public List<Map<String, Object>> getListType() {
		String sql = sqlHelper.select("`id`, `name`").from(newsTypeTable).getSelectSql();
		sqlHelper.reset();
		return jdbcTPL.queryForList(sql);
	}
	
	
	/**
	 * 新闻列表
	 * @param data
	 * @return
	 */
	public HashMap<String, Object> list(HashMap<String, String> data) {
		String cols = "id, type, title, images, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime, IF(`edittime` > 0,FROM_UNIXTIME(`edittime`, '%Y-%m-%d %h:%i'), '-') edittime";
		sqlHelper.select(cols).from(newsTable);
		if (!Util.isEmpty(data.get("type"))) {
			sqlHelper.where("type", data.get("type"));
		}
		if (!Util.isEmpty(data.get("search"))) {
			sqlHelper.like("`title`", "%"+ data.get("search") +"%");
		}
		
		sqlHelper.order("`id`", "DESC").limit(Util.toInt(data.get("page_num")), Util.toInt(data.get("page_size")));
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
	 * 获取列表, 不分页
	 * @param data
	 * @return
	 */
	public List<Map<String, Object>> getListNews(HashMap<String, String> data) {
		String cols = "id, type, title, images, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime";
		sqlHelper.select(cols).from(newsTable);
		if (!Util.isEmpty(data.get("type"))) {
			sqlHelper.where("type", data.get("type"));
		}
		if (!Util.isEmpty(data.get("id"))) {
			sqlHelper.where("`id` != ", data.get("id"));
		}		
		sqlHelper.order("`id`", "DESC").limit(Util.toInt(data.get("page_num")), Util.toInt(data.get("page_size")));
		String querySql = sqlHelper.getSelectSql();
		sqlHelper.reset();
		
		return jdbcTPL.queryForList(querySql);
	}
	

}
