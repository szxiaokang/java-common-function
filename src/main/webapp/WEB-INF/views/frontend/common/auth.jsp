<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.web.util.Util"%>
<%
	Map<String, Object> sess = new HashMap<String, Object>();
	sess = Util.getSession(request);
	System.out.println("sess: " + sess);
	if (sess == null || "".equals(sess)) {
		out.println("<script>location.href=\"/user/login.htm\";</script>");
		return;
	}
%>