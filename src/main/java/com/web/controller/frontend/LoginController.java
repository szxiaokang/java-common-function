/**
 * @ClassName:     LoginController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2016-1-18 上午11:13:39
 * @Description:   TODO
 *
 */


package com.web.controller.frontend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.base.ControllerBase;
import com.web.util.Redis;
import com.web.util.Util;

@Controller
@RequestMapping("/login/")
public class LoginController extends ControllerBase {

	private static Logger logger = Logger.getLogger("login");
	
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if ("GET".equals(request.getMethod())) {
			model.addAttribute("title", "用户登录");
			return "frontend/user/login";
		}
		String action = request.getParameter("action");
		JSONObject json = new JSONObject();

		if (Util.isEmpty(action) || !"login".equals(action)) {
			json.put("status", -1);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		httpSession = request.getSession();
		String cCaptch = request.getParameter("captch");
		String sCaptch = (String) httpSession.getAttribute("registerCaptcha");
		if (!cCaptch.equals(sCaptch)) {
			json.put("status", -2);
			model.addAttribute(jsonKey, json.toString());
			return jsonPage;
		}
 
		String password = request.getParameter("password");
		String username = request.getParameter("username");

		if (Util.isEmpty(username) || Util.isEmpty(password)) {
			json.put("status", -3);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		if (username.matches("[A-Za-z0-9_]")) {
			json.put("status", -4);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		if (Redis.get(username) == null) {
			json.put("status", -5);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		
		//写入日志
		String info = username + "\t" + Util.getIp(request);
		logger.info(info);

		//写入Session(Redis)
		Redis.setSession(username, "1");
		
		//写入cookie
		Util.setCookie(Util.getConfig("cookie.key"), username, response);

		json.put("status", 0);
		model.put(jsonKey, json.toString());
		return jsonPage;
	}
	
	@RequestMapping(value = "api")
	public String api(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "";
	}
}
