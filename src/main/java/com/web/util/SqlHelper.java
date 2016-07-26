/**
 * @ClassName:    SqlHelper.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-09 下午 16:31:12
 * @Description:   SQL 拼接助手
 *
 */

package com.web.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SqlHelper {

	protected String _select = "SELECT * ";
	protected String _selectCount = "SELECT COUNT(1) num";
	protected String _from = "";
	protected String _join = "";
	protected ArrayList<String> _where = new ArrayList<String>();
	protected ArrayList<String> _whereOr = new ArrayList<String>();
	protected String _group = "";
	protected String _having = "";
	protected String _order = "";
	protected String _limit = "";
	protected String _update = "";
	protected ArrayList<String> _error = new ArrayList<String>();
	

	public String getDeleteSql() {
		if (this._from == null || "".equals(this._from)) {
			this._error.add("getDeleteSql method, form data empty ");
			return null;
		}
		String sql = "DELETE FROM" + this._from;
		if (this._where.size() > 0 || this._whereOr.size() > 0) {
			sql += " WHERE";
		}
		sql += this.getWhere(this._where, "AND");
		if (this._whereOr.size() > 0) {
			sql += " OR";
		}
		sql += this.getWhere(this._whereOr, "OR");
		sql += this._limit;
		this.reset();
		System.out.println("Delete SQL: " + sql);
		return sql;
	}

	/**
	 * 获取插入SQL
	 * @param table 表名
	 * @param data HashMap key为字段名, value为值
	 * @return
	 */
	public String getInsertSql(String table, HashMap<String, String> data) {
		if (data.size() < 1) {
			this._error.add("getInsertSql method, arguments data empty ");
			return null;
		}

		String sql = "INSERT INTO " + table;
		int i = 0, size = data.size();
		String[] keys = new String[size];
		String[] values = new String[size];
		Iterator<Entry<String, String>> iter = data.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value == null || key == null || "".equals(key)) {
				continue;
			}
			String k = key.toString();
			if (k.indexOf('`') == -1) {
				k = "`" + k + "`";
			}
			keys[i] = k;
			values[i] = this.escape( value.toString());
			i++;
		}
		
		if (keys.length < 1 || values.length < 1) {
			return null;
		}
		sql += "("+ Util.join(keys, ", ") +") VALUES("+ Util.join(values, ", ") +")";
		System.out.println("Insert SQl: " + sql);
		this.reset();
		return sql;
	}
	
	
	/**
	 * 获取SELECT 语句
	 * 
	 * @return
	 */
	public String getSelectSql() {
		if ("".equals(this._from)) {
			this._error.add("getSelectSql method, arguments table empty ");
			return null;
		}
		String sql = this._select + " FROM" + this._from;
		sql += this._join;
		if (this._where.size() > 0 || this._whereOr.size() > 0) {
			sql += " WHERE";
		}
		sql += this.getWhere(this._where, "AND");
		if (this._whereOr.size() > 0) {
			sql += " OR";
		}
		sql += this.getWhere(this._whereOr, "OR");
		sql += this._group;
		sql += this._having;
		sql += this._order;
		sql += this._limit;
		System.out.println("Select SQl: " + sql);
		return sql;
	}

	

	/**
	 * 获取UPDATE语句
	 * @return
	 */
	public String getUpdateSql() {
		String sql = "UPDATE " + this._from + this._update;
		if (this._where.size() > 0 || this._whereOr.size() > 0) {
			sql += " WHERE";
		}
		sql += this.getWhere(this._where, "AND");
		if (this._whereOr.size() > 0) {
			sql += " OR";
		}
		sql += this.getWhere(this._whereOr, "OR");
		sql += this._limit;
		this.reset();
		System.out.println("Update SQl: " + sql);
		return sql;
	}

	/**
	 * 获取统计行数SQL 注意: 此方法会重置所有值
	 * 
	 * @return
	 */
	public String getCountSql() {
		String sql = this._selectCount + " FROM" + this._from;
		sql += this._join;
		if (this._where.size() > 0 || this._whereOr.size() > 0) {
			sql += " WHERE";
		}
		sql += this.getWhere(this._where, "AND");
		if (this._whereOr.size() > 0) {
			sql += " OR";
		}
		sql += this.getWhere(this._whereOr, "OR");
		sql += this._group;
		this.reset();
		System.out.println("Count SQl: " + sql);
		return sql;
	}

	

	/**
	 * 查询列, 默认为 *
	 * 
	 * @param cols
	 * @return
	 */
	public SqlHelper select(String cols) {
		if (cols == null || "".equals(cols)) {
			cols = "*";
		}
		this._select = "SELECT " + cols;
		return this;
	}

	/**
	 * 查询表
	 * 
	 * @param table
	 * @return
	 */
	public SqlHelper from(String table) {
		if (table == null || "".equals(table)) {
			this._error.add("from method, arguments table null or empty");
			return this;
		}
		this._from = " " + table;
		return this;
	}

	/**
	 * 连表查询
	 * 
	 * @param table 表名
	 * @param cond 条件
	 * @param joinType 连接类型
	 * @return
	 */
	public SqlHelper join(String table, String cond, String joinType) {
		if (table == null || "".equals(table)) {
			this._error.add("join method, arguments table null or empty");
			return this;
		}

		HashMap<String, Integer> types = new HashMap<String, Integer>();
		types.put("LEFT", 1);
		types.put("RIGHT", 1);
		types.put("OUTER", 1);
		types.put("INNER", 1);
		types.put("LEFT OUTER", 1);
		types.put("RIGHT OUTER", 1);
		if (types.get(joinType.toUpperCase()) == null) {
			this._error.add("join method, arguments joinType: " + joinType + " error, allow joinType: LEFT, RIGHT, OUTER, INNER, LEFT OUTER, RIGHT OUTER");
			return this;
		}
		this._join += " " + joinType + " JOIN " + table + " ON " + cond;
		return this;
	}

	/**
	 * where条件拼接
	 * 
	 * @param key 字段
	 * @param value值
	 * @return
	 */
	public SqlHelper where(String key, int value) {
		if (key == null) {
			return this;
		}
		if (key.indexOf('!') == -1 && key.indexOf('<') == -1 && key.indexOf('>') == -1) {
			key += " = ";
		}
		String cond = " (" + key + value + ")";
		this._where.add(cond);
		return this;
	}

	/**
	 * where 条件拼接
	 * 
	 * @param key 字段名称
	 * @param value 查询值, 特殊字符将被转义
	 * @return
	 */
	public SqlHelper where(String key, String value) {
		if (key == null || value == null) {
			return this;
		}
		if (key.indexOf('!') == -1 && key.indexOf('<') == -1 && key.indexOf('>') == -1) {
			key += " = ";
		}
		String cond = " (" + key + this.escape(value) + ")";
		this._where.add(cond);
		return this;
	}

	/**
	 * LIKE查询
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SqlHelper like(String key, String value) {
		if (key == null || value == null) {
			return this;
		}

		String cond = " (" + key + " LIKE " + this.escape(value) + ")";
		this._where.add(cond);
		return this;
	}

	public SqlHelper likeOr(String key, String value) {
		if (key == null || value == null) {
			return this;
		}

		String cond = " (" + key + " LIKE " + this.escape(value) + ")";
		this._whereOr.add(cond);
		return this;
	}

	/**
	 * WHERE IN 查询
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SqlHelper whereIn(String key, String[] value) {
		if (key == null || value == null) {
			return this;
		}
		String val = "";
		int len = value.length;
		for (int i = 0; i < len; i++) {
			val += this.escape(value[i]);
			if (i + 1 < len) {
				val += ", ";
			}
		}
		
		String cond = " (" + key + " IN(" + val + "))";
		this._where.add(cond);
		return this;
	}

	/**
	 * WHERE OR 查询
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SqlHelper whereOr(String key, String value) {
		if (key == null || value == null) {
			return this;
		}
		if (key.indexOf('!') == -1 && key.indexOf('<') == -1 && key.indexOf('>') == -1) {
			key += " = ";
		}
		String cond = " (" + key + this.escape(value) + ")";
		this._whereOr.add(cond);
		return this;
	}

	/**
	 * WHERE NOT IN 查询
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SqlHelper whereNotIn(String key, String[] value) {
		if (key == null || value == null) {
			return this;
		}
		String val = "";
		int len = value.length;
		for (int i = 0; i < len; i++) {
			val += this.escape(value[i]);
			if (i + 1 < len) {
				val += ", ";
			}
		}
		
		String cond = " (" + key + " IN(" + val + "))";
		this._where.add(cond);
		return this;
	}

	/**
	 * HAVING
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SqlHelper having(String key, String value) {
		if (key == null || value == null) {
			return this;
		}
		if (key.indexOf('!') == -1 && key.indexOf('<') == -1 && key.indexOf('>') == -1) {
			key += " = ";
		}
		this._having = " HAVING (" + key + value + ")";
		return this;
	}

	/**
	 * 分组
	 * 
	 * @param key
	 * @return
	 */
	public SqlHelper group(String key) {
		if (key == null) {
			return this;
		}
		this._group = " GROUP BY " + key;
		return this;
	}

	/**
	 * 排序
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	public SqlHelper order(String key, String type) {
		if (key == null || type == null) {
			return this;
		}
		if (!"DESC".equals(type.toUpperCase()) || "ASC".equals(type.toUpperCase())) {
			this._error.add("order method, arguments type unkonw: " + type);
			return this;
		}
		this._order = " ORDER BY " + key + " " + type;
		return this;
	}

	/**
	 * limit
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public SqlHelper limit(int offset, int size) {
		offset = (offset - 1) * size;
		this._limit = " LIMIT " + (offset > 0 ? offset : 0) + ", " + size;
		return this;
	}

	/**
	 * 更新字段, key 为字段名, value为更新值
	 * @param data
	 * @return
	 */
	public SqlHelper update(String table, HashMap<String, String> data) {
		if (data.size() < 1) {
			this._error.add("update method, arguments data empty ");
			return this;
		}
		this._from = table;
		this._update = " SET ";
		Iterator<Entry<String, String>> iter = data.entrySet().iterator();
		int k = 0, size = data.size();
		while (iter.hasNext()) {
			k++;
			Entry<String, String> entry = iter.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (value == null || key == null || "".equals(key)) {
				continue;
			}
			this._update += "`" + key + "` = ";
			String val = value.toString();
			if (val.indexOf('+') != -1 || val.indexOf('-') != -1) {
				this._update += val;
			} else {
				this._update += this.escape(val);
			}
			if (k < size) {
				this._update += ", ";
			}
		}
		return this;
	}
	
	
	/**
	 * 重置
	 */
	public void reset() {
		this._select = "SELECT *";
		this._from = "";
		this._join = "";
		this._where = new ArrayList<String>();
		this._whereOr = new ArrayList<String>();
		this._group = "";
		this._having = "";
		this._order = "";
		this._limit = "";
		this._update = "";
	}

	/**
	 * 拼接WHERE
	 * 
	 * @param where
	 * @param tag
	 * @return
	 */
	private String getWhere(ArrayList<String> where, String tag) {
		String _where = "";
		int len = where.size();
		if (len == 0) {
			return _where;
		}
		if (len == 1) {
			_where = where.get(0).toString();
			return _where;
		}
		for (int i = 0; i < len; i++) {
			_where += where.get(i);
			if (i + 1 < len) {
				_where += " " + tag + "";
			}
		}

		return _where;
	}

	/**
	 * 获取错误
	 * 
	 * @return
	 */
	public String error() {
		if (this._error.size() > 0) {
			String err = "";
			for (int i = 0; i < this._error.size(); i++) {
				err += this._error.get(i) + "\n";
			}
			return err;
		}
		return "";
	}

	private String escape(String x) {
		if (x == null || "".equals(x)) {
			return "''";
		}

		int len = x.length();
		boolean isNeedEscape = false;
		for (int i = 0; i < len; i++) {
			char c = x.charAt(i);
			isNeedEscape = c == 0 || c == '\n' || c == '\r' || c == '\\' || c == '\'' || c == '\032' || c == '\u00a5' || c == '\u20a9';
			if (isNeedEscape) {
				break;
			}
		}
		if (!isNeedEscape) {
			return "'" + x + "'";
		}

		StringBuilder buf = new StringBuilder((int) (len * 1.1));
		buf.append('\'');
		for (int i = 0; i < len; ++i) {
			char c = x.charAt(i);
			switch (c) {
			case 0:
				buf.append('\\');
				buf.append('0');
				break;

			case '\n':
				buf.append('\\');
				buf.append('n');
				break;

			case '\r':
				buf.append('\\');
				buf.append('r');
				break;

			case '\\':
				buf.append('\\');
				buf.append('\\');
				break;
			case '\'':
				buf.append('\\');
				buf.append('\'');
				break;

			case '"':
				buf.append('\\');
				buf.append('"');
				break;

			case '\032':
				buf.append('\\');
				buf.append('Z');
				break;

			case '\u00a5':
			case '\u20a9':
				// escape characters interpreted as backslash by mysql
				CharBuffer cbuf = CharBuffer.allocate(1);
				ByteBuffer bbuf = ByteBuffer.allocate(1);
				cbuf.put(c);
				cbuf.position(0);
				if (bbuf.get(0) == '\\') {
					buf.append('\\');
				}
				// fall through

			default:
				buf.append(c);
			}
		}

		buf.append('\'');
		return buf.toString();
	}

}
