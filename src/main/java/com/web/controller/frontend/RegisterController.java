/**
 * @ClassName:     RegisterController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2016-1-15 下午2:46:07
 * @Description:   TODO
 * 用户注册
 * 
 */


package com.web.controller.frontend;

import java.util.HashMap;

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
@RequestMapping("/register/")
public class RegisterController extends ControllerBase {
	private static Logger logger = Logger.getLogger("register");
	
	
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if ("GET".equals(request.getMethod())) {
			model.addAttribute("title", "用户注册");
			return "frontend/user/register";
		}
		String action = request.getParameter("action");
		JSONObject json = new JSONObject();

		if (Util.isEmpty(action) || !"register".equals(action)) {
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
		String repassword = request.getParameter("repassword");
		String username = request.getParameter("username");

		if (Util.isEmpty(username) || Util.isEmpty(password) || Util.isEmpty(repassword)) {
			json.put("status", -3);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		if (username.matches("[A-Za-z0-9_]+")) {
			json.put("status", -4);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		if (!password.equals(repassword)) {
			json.put("status", -5);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		if (Redis.get(username) != null) {
			json.put("status", -6);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		
		//写入日志
		String info = username + "\t" + Util.MD5( password) + "\t" + Util.getTime() + "\t" + Util.getIp(request);
		logger.info(info);
		
		//写入Redis
		Redis.set(username, Util.MD5(password));
		
		//写入Session(Redis)
		Redis.setSession(username, "1");

		json.put("status", 0);
		model.put(jsonKey, json.toString());
		return jsonPage;
	}
	
	@RequestMapping(value = "api")
	public String api(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		return "";
	}

}
