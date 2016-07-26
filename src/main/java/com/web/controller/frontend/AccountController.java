/**
 * @ClassName:     AccountController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-23 上午9:36:17
 * @Description:   TODO
 *
 */

package com.web.controller.frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.base.ControllerBase;
import com.web.database.Users;
import com.web.util.Redis;
import com.web.util.Util;

@Controller
@RequestMapping("/user/")
public class AccountController extends ControllerBase {

	public Map<String, Object> userSession = null;
	Users user = new Users();

	/**
	 * 用户首页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("title", "最新注册用户");
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("page_size", "10");
		filter.put("page_num", "1");
		model.addAttribute("rows", user.getList(filter));
		return "frontend/user/index";
	}

	/**
	 * 登录后的主页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "main")
	public String main(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String value = Redis.getSession(request); 
		if (value == null) {
			return redirectLogin(request);
		}
		model.addAttribute("title", "基本信息");
		model.addAttribute("row", user.getUser(value));
		return "frontend/user/main";
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login")
	public String login(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		if ("GET".equals(request.getMethod())) {
			String url = request.getParameter("url");
			url = !Util.isEmpty(url) ? Util.decodeBase64URL(url) : "/user/main.htm";
			model.addAttribute("url", url);
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

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("password", request.getParameter("password"));
		data.put("email", request.getParameter("email"));

		if (Util.isEmpty(data.get("email")) || Util.isEmpty(data.get("password"))) {
			json.put("status", -3);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		if (!Util.isEmail(data.get("email"))) {
			json.put("status", -4);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		Map<String, Object> userRow = user.login(data.get("email"), Util.MD5(data.get("password")));
		if (userRow == null) {
			json.put("status", -5);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		HashMap<String, String> userUpdate = new HashMap<String, String>();
		String uid = userRow.get("uid").toString();
		userUpdate.put("last_ip", Util.getIp(request));
		userUpdate.put("login_num", String.valueOf(Util.toInt(userRow.get("login_num")) + 1));
		userUpdate.put("last_login", Util.getTime().toString());
		user.update(Util.toInt(userRow.get("uid")), userUpdate);

		// to memcache
		Util.setSession(uid, userRow);
		// to cookie
		Util.setCookie(Util.getConfig("cookie.key"), uid, response);

		json.put("status", 0);
		model.put(jsonKey, json.toString());
		return jsonPage;
	}

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "register")
	public String register(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

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

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("password", request.getParameter("password"));
		data.put("repassword", request.getParameter("repassword"));
		data.put("email", request.getParameter("email"));

		if (Util.isEmpty(data.get("email")) || Util.isEmpty(data.get("password")) || Util.isEmpty(data.get("repassword"))) {
			json.put("status", -3);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		if (!Util.isEmail(data.get("email"))) {
			json.put("status", -4);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		if (!data.get("password").equals(data.get("repassword"))) {
			json.put("status", -5);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		if (user.checkEmail(data.get("email"))) {
			json.put("status", -6);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		data.remove("repassword");
		data.put("addtime", Util.getTime().toString());
		data.put("last_ip", Util.getIp(request));
		data.put("password", Util.MD5(data.get("password")));

		json.put("status", -7);
		int uid = user.insert(data);
		if (uid > 0) {
			HashMap<String, Object> session = new HashMap<String, Object>();
			session.put("email", data.get("email"));
			session.put("addtime", data.get("addtime"));
			session.put("uid", uid);
			// to memcache
			Util.setSession(String.valueOf(uid), session);
			// to cookie
			Util.setCookie(Util.getConfig("cookie.key"), String.valueOf(uid), response);
			json.put("status", 0);
		}
		model.put(jsonKey, json.toString());
		return jsonPage;
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	/*@RequestMapping(value = "password")
	public String password(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		userSession = Util.getSession(Util.getConfig("cookie.key"), request);
		if (userSession == null) {
			return redirectLogin(request);
		}
		
		int uid = Util.toInt(userSession.get("uid"));
		if ("GET".equals(request.getMethod())) {
			model.addAttribute("title", "修改密码");
			return "frontend/user/password";
		}
		String action = request.getParameter("action");
		JSONObject json = new JSONObject();

		if (Util.isEmpty(action) || !"password".equals(action)) {
			json.put("status", -1);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("password", request.getParameter("password"));
		data.put("repassword", request.getParameter("repassword"));
		data.put("oldpassword", request.getParameter("oldpassword"));

		if (Util.isEmpty(data.get("oldpassword")) || Util.isEmpty(data.get("password")) || Util.isEmpty(data.get("repassword"))) {
			json.put("status", -2);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		if (!data.get("password").equals(data.get("repassword"))) {
			json.put("status", -3);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}

		Map<String, Object> userRow = user.getUser(uid);
		if (!Util.MD5(data.get("oldpassword")).equals(userRow.get("password").toString())) {
			json.put("status", -4);
			model.put(jsonKey, json.toString());
			return jsonPage;
		}
		data.remove("repassword");
		data.remove("oldpassword");
		data.put("password", Util.MD5(data.get("password")));
		json.put("status", -5);
		if (user.update(uid, data) > 0) {
			json.put("status", 0);
		}
		model.put(jsonKey, json.toString());
		return jsonPage;
	}*/
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "logout")
	public void logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		userSession = Util.getSession(Util.getConfig("cookie.key"), request);
		if (userSession == null) {
			response.sendRedirect("/user/index.htm");
			return;
		}
		String uid = userSession.get("uid").toString();
		Util.removeSession(uid);
		Util.removeCookie(Util.getConfig("cookie.key"), response);
		response.sendRedirect("/user/index.htm");
		return;
	}

}
