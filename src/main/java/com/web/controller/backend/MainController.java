/**
 * @ClassName:     LoginController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-15 下午5:01:47
 * @Description:   TODO
 *
 */

package com.web.controller.backend;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.web.base.ControllerBase;
import com.web.database.Admins;
import com.web.util.Captcha;
import com.web.util.Util;

@Controller
@RequestMapping("/admin/")
public class MainController extends ControllerBase {
	
	private static Logger logger = Logger.getLogger("user");

	/**
	 * 管理员登录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		if ("GET".equals(request.getMethod())) {
			return "backend/common/login";
		}
		
		String action = request.getParameter("action");
		String ip = Util.getIp(request);
		logger.info(ip + " login request");
		JSONObject json = new JSONObject();
		if (Util.isEmpty(action) || !"login".equals(action)) {
			logger.info(ip + " login status: -1");
			json.put("status", -1);
			model.addAttribute(jsonKey, json.toString());
			return jsonPage;
		}
		httpSession = request.getSession();
		if (httpSession.getAttribute("loginCaptch") == null) {
			logger.info(ip + " login status: -2");
			json.put("status", -2);
			model.addAttribute(jsonKey, json.toString());
			return jsonPage;
		}
		String cCode = request.getParameter("code");
		String sCode = (String) httpSession.getAttribute("loginCaptch");
		if (!sCode.equals(cCode)) {
			logger.info(ip + " login status: -2");
			json.put("status", -2);
			model.addAttribute(jsonKey, json.toString());
			return jsonPage;
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (Util.isEmpty(username) || Util.isEmpty(password)) {
			logger.info(ip + " login status: -3");
			json.put("status", -3);
			model.addAttribute(jsonKey, json.toString());
			return jsonPage;
		}
		
		Admins admin = new Admins();
		Map<String, Object> data = admin.adminLogin(username, Util.MD5(password), ip);
		if (data == null) {
			logger.info(ip + " login status: -4");
			json.put("status", -4);
			model.addAttribute(jsonKey, json.toString());
			return jsonPage;
		}
		logger.info(ip + " login status: 0");
		// to memcache
		String uid = "adminid_" + String.valueOf(data.get("adminid"));
		Util.setSession(uid, data);
		
		// to cookie
		Util.setCookie(Util.getConfig("cookie.admin.key"), uid, response);
		json.put("status", 0);
		model.addAttribute(jsonKey, json.toString());
		return jsonPage;
	}
	
	/**
	 * 关于
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "about")
	public String about(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		return "backend/common/about";
	}
	
	/**
	 * 主页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "main")
	public String main(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Admins admin = new Admins();
		model.addAttribute("data", admin.count());
		return "backend/common/main";
	}
	
	/**
	 * 登出
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "logout")
	public void logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		Map<String, Object> adminSession = Util.getSession(Util.getConfig("cookie.admin.key"), request);
		if (adminSession == null) {
			response.sendRedirect("/admin/index.htm");
			return;
		}
		String adminid = "adminid_" + adminSession.get("adminid").toString();
		Util.removeSession(adminid);
		Util.removeCookie(Util.getConfig("cookie.admin.key"), response);
		response.sendRedirect("/admin/index.htm");
		return;
	}

	/**
	 * 验证码程序
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "captch", method = RequestMethod.GET)
	public void captch(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Captcha code = new Captcha();
		code.captcha1(request, response, "loginCaptch", 4);
	}

}
