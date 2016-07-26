package com.web.util;

import javax.servlet.http.HttpServletRequest;

public class Pager {

	public int pageCount = 1;
	public int pageSize = 10;
	public int recordCount = 1;
	public String tag = "page";
	public String firstText = "<span>第一页</span>";
	public String prevText = "<span>上一页</span>";
	public String nextText = "<span>下一页</span>";
	public String lastText = "<span>最后一页</span>";
	public String firstHref = "";
	public String prevHref = "";
	public String nextHref = "";
	public String lastHref = "";

	HttpServletRequest request;

	public Pager(HttpServletRequest Request, int RecordCount, int PageSize) {
		this.request = Request;
		this.recordCount = RecordCount;
		this.pageSize = PageSize;
		if (RecordCount > 0 && PageSize > 0 && RecordCount > PageSize) {
			this.pageCount = (int) Math.ceil((double) RecordCount / PageSize);
			this.processURL();
		} else {
			return;
		}
	}

	public void processURL() {
		int current = 1, prev = 1;
		if (request.getParameter(this.tag) != null) {
			current = Util.toInt(request.getParameter(this.tag));
		}
		int next = current + 1;
		if (current > 1) {
			prev = current - 1;
		}
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		String format = uri + "?" + this.tag + "=%d";
		if (query == null) {
			this.nextHref = String.format(format, next);
			this.lastHref = String.format(format, this.pageCount);
			return;
		}
		
		String currString = this.tag + "=" + current;
		format = uri + "?" + query.replace(this.tag + "=" + current, "");
		if (query.indexOf(currString) == -1) {
			format += "&";
		}
		this.firstHref = format + this.tag + "=1";
		this.prevHref = format + this.tag + "=" + prev;
		this.nextHref = format + this.tag + "=" + next;
		this.lastHref = format + this.tag + "=" + this.pageCount;
	}

	private String getLink(String url, String text) {
		if (url == null || "".equals(url)) {
			return String.format("<li>%s</li>", text);
		}
		return String.format("<li><a href=\"%s\">%s</a></li>", url, text);
	}

	public String getPager() {
		int current = 1;
		if (request.getParameter(this.tag) != null) {
			current = Util.toInt(request.getParameter(this.tag));
		}
		String html = "<ul class=\"pager\">";
		if (current == 1) {
			html += this.getLink(null, this.firstText);
			html += this.getLink(null, this.prevText);
			html += this.getLink(this.nextHref, this.nextText);
			html += this.getLink(this.lastHref, this.lastText);
		} else if (current == this.pageCount) {
			html += this.getLink(this.firstHref, this.firstText);
			html += this.getLink(this.prevHref, this.prevText);
			html += this.getLink(null, this.nextText);
			html += this.getLink(null, this.lastText);
		} else {
			html += this.getLink(this.firstHref, this.firstText);
			html += this.getLink(this.prevHref, this.prevText);
			html += this.getLink(this.nextHref, this.nextText);
			html += this.getLink(this.lastHref, this.lastText);
		}
		html += "<li> 共 " + this.recordCount + " 记录, " + this.pageCount + "页, 转到: ";
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		if (query == null) {
			query = "";
		}
		query = query.replace(this.tag + "=" + current, "");
		html += "<select onChange=\"location.href=this.value\">";
		for (int k = 1; k <= this.pageCount; k++) {
			String pageURL = uri + "?";
			if (query != null && !"".equals(query)) {
				pageURL += query + "&" + this.tag + "=" + k;
			} else {
				pageURL += this.tag + "=" + k;
			}
			html += "<option value=\"" + pageURL + "\"";
			if (current == k) {
				html += " selected";
			}
			html += ">" + k + "</option>";
		}
		html += "</select></li>";
		html += "</ul>";

		return html;
	}

}
