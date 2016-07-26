/**
 * @ClassName:     NewsController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-10 下午2:19:03
 * @Description:   新闻相关
 * 添加/修改/删除/管理/分类添加/分类修改/分类删除/分类管理
 * 
 */

package com.web.controller.backend;

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
import com.web.database.News;
import com.web.util.Pager;
import com.web.util.UploadFile;
import com.web.util.Util;

@Controller
@RequestMapping("/admin/news/")
public class NewsController extends ControllerBase {

	private static Logger logger = Logger.getLogger("user");
	News news = new News();

	
	/**
	 * 新闻添加
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "add")
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String action = request.getParameter("action");
		
		if ("POST".equals(request.getMethod())) {
			JSONObject json = new JSONObject();
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("title", request.getParameter("title"));
			data.put("intro", request.getParameter("sub_title"));
			data.put("type", request.getParameter("type"));
			data.put("content", request.getParameter("content"));
			data.put("images", request.getParameter("images"));

			if ("add".equals(action)) {
				data.put("addtime", Util.getTime().toString());
				json.put("status", news.insert(data));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}

			if ("save".equals(action)) {
				int id = Util.toInt(request.getParameter("id"));
				if (id == -1) {
					json.put("status", -1);
					model.addAttribute(jsonKey, json.toString());
					return jsonPage;
				}
				data.put("edittime", Util.getTime().toString());
				json.put("status", news.update(data, Util.toInt(id)));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}

		}

		// GET 显示页面或编辑
		if ("edit".equals(action)) {
			int id = Util.toInt(request.getParameter("id"));
			String url = request.getParameter("url");
			if (id == -1 || Util.isEmpty(url)) {
				model.addAttribute("msg", "id或url参数不正确");
				return this.adminAlert;
			}

			Map<String, Object> row = news.getNews(Util.toInt(id));
			row.put("url", Util.decodeBase64URL(request.getParameter("url")));
			model.addAttribute("action", "save");
			model.addAttribute("row", row);
		}
		
		model.addAttribute("types", news.getListType());
		return "backend/news/add";
	}

	/**
	 * 新闻管理
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if ("POST".equals(request.getMethod())) {
			// delete
			String action = request.getParameter("action");
			if (action != null && "delete".equals(action)) {
				String[] ids = request.getParameterValues("ids");
				String url = request.getParameter("url");
				if (ids.length == 0 || url == null || "".equals(url)) {
					return this.adminAlert;
				}
				if (news.delete(ids) == -1) {
					model.addAttribute("msg", "传入参数不正确");
					return this.adminAlert;
				}
				logger.info("delete news, id: " + Util.join(ids, ", "));
				model.addAttribute("url", Util.decodeBase64URL(url));
				return this.adminRedirect;
			}
		}

		// 获取列表
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("search", request.getParameter("search"));
		data.put("type", request.getParameter("type"));
		data.put("page_size", String.valueOf(this.ADMIN_PAGESIZE));
		data.put("page_num", request.getParameter("page"));

		HashMap<String, Object> result = news.list(data);
		int count = Util.toInt(result.get("count"));
		Pager paging = new Pager(request, count, this.ADMIN_PAGESIZE);
		model.put("paging", paging.getPager());
		model.put("rows", result.get("rows"));
		model.put("types", news.getListType());
		model.put("url", Util.getURL(request));

		return "backend/news/list";
	}

	/**
	 * 添加新闻类型
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addType")
	public String addType(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String action = request.getParameter("action");
		if ("POST".equals(request.getMethod())) {
			JSONObject json = new JSONObject();
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("name", request.getParameter("name"));

			if ("validatorName".equals(action)) {
				data.put("id", request.getParameter("id"));
				json.put("status", news.checkName(data.get("name")));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}

			if ("add".equals(action)) {
				data.put("addtime", Util.getTime().toString());
				json.put("status", news.insertType(data));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}

			if ("save".equals(action)) {
				int id = Util.toInt(request.getParameter("id"));
				if (id == -1) {
					json.put("status", -1);
					model.addAttribute(jsonKey, json.toString());
					return jsonPage;
				}
				data.put("edittime", Util.getTime().toString());
				json.put("status", news.updateType(data, Util.toInt(id)));
				model.addAttribute(jsonKey, json.toString());
				return jsonPage;
			}

		}

		// GET 显示页面或编辑
		if ("edit".equals(action)) {
			int id = Util.toInt(request.getParameter("id"));
			String url = request.getParameter("url");
			if (id == -1 || Util.isEmpty(url)) {
				model.addAttribute("msg", "id或url参数不正确");
				return this.adminAlert;
			}

			Map<String, Object> row = news.getType(Util.toInt(id));
			row.put("url", Util.decodeBase64URL(request.getParameter("url")));
			model.addAttribute("action", "save");
			model.addAttribute("row", row);
		}

		return "backend/news/add_type";
	}

	/**
	 * 新闻类型管理
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "listType")
	public String listType(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if ("POST".equals(request.getMethod())) {
			// delete
			String action = request.getParameter("action");
			if (action != null && "delete".equals(action)) {
				String[] ids = request.getParameterValues("ids");
				String url = request.getParameter("url");
				if (ids.length == 0 || url == null || "".equals(url)) {
					return this.adminAlert;
				}
				if (news.deleteType(ids) == -1) {
					model.addAttribute("msg", "传入参数不正确");
					return this.adminAlert;
				}
				logger.info("delete news type, id: " + Util.join(ids, ", "));
				model.addAttribute("url", Util.decodeBase64URL(url));
				return this.adminRedirect;
			}
		}

		// 获取列表
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("search", request.getParameter("search"));
		data.put("page_size", String.valueOf(this.ADMIN_PAGESIZE));
		data.put("page_num", request.getParameter("page"));

		HashMap<String, Object> result = news.listType(data);
		int count = Util.toInt(result.get("count"));
		Pager paging = new Pager(request, count, this.ADMIN_PAGESIZE);
		model.put("paging", paging.getPager());
		model.put("rows", result.get("rows"));
		model.put("url", Util.getURL(request));

		return "backend/news/list_type";
	}

	/**
	 * 文件上传
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "uploadFile")
	public String uploadFile(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		UploadFile upload = new UploadFile();
		String json = upload.uploadFiles(request);
		model.addAttribute(jsonKey, json);
		return this.jsonPage;
	}

	/**
	 * 文件管理
	 * @param request
	 * @param response
	 * @param model
	 * @return json
	 */
	@RequestMapping(value = "fileManage")
	public String fileManage(HttpServletRequest request, HttpServletResponse response, ModelMap model) {		
		UploadFile upload = new UploadFile();
		String json = upload.fileManage(request);
		model.addAttribute(jsonKey, json);
		return this.jsonPage;
	}
	
}
