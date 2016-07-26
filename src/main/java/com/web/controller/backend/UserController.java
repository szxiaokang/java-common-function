/**
 * @ClassName:     UserController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-18 下午12:37:56
 * @Description:   TODO
 *
 */

package com.web.controller.backend;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.base.ControllerBase;
import com.web.database.Users;
import com.web.util.Pager;
import com.web.util.Util;

@Controller
@RequestMapping("/admin/user/")
public class UserController extends ControllerBase {

	private static Logger logger = Logger.getLogger("user");
	
	Users user = new Users();
	

	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 获取列表
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("search", request.getParameter("search"));
		data.put("type", request.getParameter("type"));
		data.put("page_size", String.valueOf(this.ADMIN_PAGESIZE));
		data.put("page_num", request.getParameter("page"));

		HashMap<String, Object> result = user.list(data);
		int count = Util.toInt(result.get("count"));
		Pager paging = new Pager(request, count, this.ADMIN_PAGESIZE);
		model.put("paging", paging.getPager());
		model.put("rows", result.get("rows"));		
		model.put("url", Util.getURL(request));
		return "backend/user/list";
	}
}
