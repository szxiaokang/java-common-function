/**
 * @ClassName:     Filter.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-25 下午1:57:29
 * @Description:   拦截器
 *
 */


package com.web.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.web.util.Util;


public class Filter implements HandlerInterceptor
{

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		//判断后台是否登录
		String uri = request.getRequestURI();
		if (uri == null || "".equals(uri)) {
			return true;
		}
		System.out.println("URI: " + uri);
		String[] uris = uri.split("/");
		System.out.print("uris: " + uris[0] + "|" + uris[1]);
		if ("admin".equalsIgnoreCase(uris[1])) {
			String action = "index.htm";
			if (uris.length > 2) {
				action = uris[2];
			}
			Map<String, Object> adminSession = Util.getSession(Util.getConfig("cookie.admin.key"), request);
			System.out.println("action: " + action);
			System.out.println("adminSsession: " + adminSession);
			if (adminSession == null && !action.equalsIgnoreCase("index.htm") && !action.equalsIgnoreCase("captch.htm") && !action.equalsIgnoreCase("logout.htm")) {
				response.sendRedirect("/admin/index.htm");
				return false;
			}
		}
		
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
		
	}
}
