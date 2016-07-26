package com.web.controller.backend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.base.ControllerBase;
import com.web.database.Admins;
import com.web.util.SqlHelper;
import com.web.util.Util;
import com.web.util.Pager;

@Controller
@RequestMapping("/admin/admin/")
public class AdminController extends ControllerBase {

	Admins admin = new Admins();
	private static Logger logger = Logger.getLogger("user");

	@RequestMapping(value = "add")
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String action = request.getParameter("action");
		
		//POST 添加或修改
		if ("POST".equals(request.getMethod())) {
			JSONObject json = new JSONObject();
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("username", request.getParameter("username"));
			data.put("password", Util.MD5(request.getParameter("password")));
			data.put("repassword", Util.MD5(request.getParameter("repassword")));
			data.put("email", request.getParameter("email"));

			if ("validatorUsername".equals(action)) {
				json.put("status", admin.checkUserName(data.get("username")));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}
			// 添加
			if ("add".equals(action)) {
				data.put("addtime", Util.getTime().toString());
				json.put("status", admin.insertAdmin(data));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}
			//修改
			if ("save".equals(action)) {
				data.remove("repassword");
				if ("######".equals(request.getParameter("password"))) {
					data.remove("password");
				}
				int adminid = Util.toInt(request.getParameter("adminid"));
				data.remove("username");
				data.put("edittime", Util.getTime().toString());
				json.put("status", admin.updateAdmin(data, adminid));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}
		}
		
		//GET 显示页面或编辑
		if ("edit".equals(action)) {
			String _id = request.getParameter("id");
			String url = request.getParameter("url"); 
			if (Util.isEmpty(_id) || Util.isEmpty(url)) {
				model.addAttribute("msg", "id或url参数不正确");
				return this.adminAlert;
			}
			
			int id = Integer.parseInt(_id);
			Map<String, Object> row = admin.getAdmin(id);
			model.addAttribute("action", "save");
			model.addAttribute("row", row);
			model.addAttribute("url", Util.decodeBase64URL(url));
		}
		
		return "backend/admin/add";
	}

	/**
	 * 管理员列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		if ("POST".equals(request.getMethod())) {
			// delete
			String action = request.getParameter("action");
			if (action != null && "delete".equals(action)) {
				String[] ids = request.getParameterValues("ids");
				String url = request.getParameter("url");
				if (ids.length == 0 || url == null || "".equals(url)) {
					return this.adminAlert;
				}
				if (admin.delete(ids) == -1) {
					model.addAttribute("msg", "传入参数不正确");
					return this.adminAlert;
				}
				logger.info("delete administrator, id: " + Util.join(ids, ", "));
				System.out.println(Util.decodeBase64URL(url));
				model.addAttribute("url", Util.decodeBase64URL(url));
				return this.adminRedirect;
			}
		}

		// 获取列表
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("search", request.getParameter("search"));
		data.put("page_size", String.valueOf(this.ADMIN_PAGESIZE));
		data.put("page_num", request.getParameter("page"));
		HashMap<String, Object> result = admin.listAdmin(data);
		int count = Util.toInt(result.get("count"));
		Pager paging = new Pager(request, count, this.ADMIN_PAGESIZE);
		model.put("paging", paging.getPager());
		model.put("rows", result.get("rows"));
		model.put("url", Util.getURL(request));

		return "backend/admin/list";
	}

	@RequestMapping(value = "test")
	public String test(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String search = "";
		if (request.getParameter("search") != null) {
			search = request.getParameter("search");
		}
		SqlHelper sql = new SqlHelper();
		String cols = "`adminid`, `username`, `email`, FROM_UNIXTIME(`addtime`, '%Y-%m-%d %h:%i') addtime, IF(`edittime` > 0,FROM_UNIXTIME(`edittime`, '%Y-%m-%d %h:%i'), '-') edittime";
		String querySql = sql.select(cols).from("admin").where("username", search).getSelectSql();
		String countSql = sql.getCountSql();
		System.out.println("querySql: " + querySql);
		System.out.println("countSql: " + countSql);
		sql.reset(); // 重置

		// 新闻查询
		cols = "n.iid, n.stitle, n.ssubhead, n.ipopularnum, n.simage, n.icollectionnum, n.ipraisenum, n.icommentstotal, nc.iclassid itypeid";
		sql.select(cols);
		sql.from("tb_news n");
		sql.join("tb_newsclass nc", "nc.inewsid = n.iid", "INNER");
		// sql.where("nc.iclassid", 16);
		// sql.where("n.stitle", search).like("n.subhead", "%" + search + "").likeOr("n.iaddtime", "%1444899");
		sql.like("n.subhead", "%" + search + "").likeOr("n.iaddtime", "%1444899");
		sql.order("n.iid", "DESC");
		sql.limit(1, 10);
		querySql = sql.getSelectSql();
		countSql = sql.getCountSql();
		System.out.println("querySql: " + querySql);
		System.out.println("countSql: " + countSql);
		sql.reset(); // 重置

		model.addAttribute("sql", querySql);

		return "backend/common/sqltest";
	}

}
