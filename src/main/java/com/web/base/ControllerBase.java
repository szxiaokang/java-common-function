/**
 * @ClassName:     NewsController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-10 下午2:19:03
 * @Description:   TODO
 *
 */
package com.web.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.util.Util;

public class ControllerBase {
	public String jsonPage = "common/json";
	public String adminAlert = "backend/common/alert";
	public String adminRedirect = "backend/common/redirect";
	public String frontError = "frontend/common/error";
	public String jsonKey = "json";
	protected int ADMIN_PAGESIZE = 10;
	public HttpSession httpSession = null;
	public String loginURL = "redirect:/user/login.htm?url=";

	public ControllerBase() {
		/* 时区 */
		System.setProperty("user.timezone", "Asia/Shanghai");
	}

	public String redirectLogin(HttpServletRequest request) {
		return loginURL + Util.getURL(request);
	}

}
