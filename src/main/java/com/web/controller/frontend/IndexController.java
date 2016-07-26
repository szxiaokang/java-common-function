/**
 * @ClassName:     IndexController.java
 * @author         KangYun 273030282@qq.com
 * @version        V1.0 
 * @Date           2015-12-22 上午11:13:24
 * @Description:   TODO
 *
 */

package com.web.controller.frontend;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;

import com.web.base.ControllerBase;
import com.web.database.News;
import com.web.util.Captcha;
import com.web.util.Redis;
import com.web.util.Util;

@Controller
public class IndexController extends ControllerBase {

	News news = new News();

	/**
	 * 主页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		model.addAttribute("title", "主页");
		
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("page_num", "1");
		filter.put("page_size", "10");
		filter.put("type", request.getParameter("type"));

		model.put("rows", news.getListNews(filter));
		model.put("types", news.getListType());
		return "frontend/index/index";
	}

	/**
	 * 新闻详细页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("id");
		if (Util.isEmpty(id)) {
			return frontError;
		}
		Map<String, Object> data = news.getNews(Util.toInt(id));
		if (data == null) {
			return frontError;
		}
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("page_num", "1");
		filter.put("page_size", "10");
		filter.put("type", request.getParameter("type"));
		filter.put("id", id);
		model.put("row", data);
		model.put("title", data.get("title"));
		model.put("rows", news.getListNews(filter));
		return "frontend/index/detail";
	}
	
	/**
	 * 验证码程序
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "captch", method = RequestMethod.GET)
	public void captch(HttpServletRequest request, HttpServletResponse response) {
		Captcha code = new Captcha();
		code.captcha1(request, response, "registerCaptcha", 4);
	}
	
	@RequestMapping(value = "about", method = RequestMethod.GET)
	public String about(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("title", "关于我们");
		return "frontend/about/index";
	}
	
	@RequestMapping(value = "/404")
	public String error404(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		return "";
	}
	
	@RequestMapping(value = "/50x")
	public String error50x(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		return "";
	}
	
	@RequestMapping(value = "/redis")
	public String redis(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.addAttribute("title", "Redis 查询");
		//Jedis redis = new Jedis(Util.getConfig("redis.slave.ip"), Util.toInt(Util.getConfig("redis.slave.port")));
		
		if ("POST".equals(request.getMethod())) {
			String key = request.getParameter("key");
			if (!Util.isEmpty(key)) {
				model.addAttribute("key", key);
				model.addAttribute("value", Redis.get(key));
			}
		}
		return "frontend/about/redis";
	}
}
