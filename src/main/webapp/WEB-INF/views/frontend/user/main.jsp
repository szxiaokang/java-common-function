<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage=""%>
<jsp:include page="../common/header.jsp" />
  <%
HashMap<String, Object> row = new HashMap<String, Object>();
if (request.getAttribute("row") != null) {
	row = (HashMap) request.getAttribute("row");
}
%>
  <div class="row" style="margin-top:20px;">
    <div class="col-xs-1"></div>
    <div class="col-xs-6" style="line-height:40px;">
      <h4>基本信息</h4>
      <div class="row">
        <div class="col-sm-2 text-right"><b>Email</b></div>
        <div class="col-sm-10"> <%=row.get("email") == null ? "" : row.get("email")%> </div>
      </div>
      <div class="row">
        <div class="col-sm-2 text-right"><b>注册时间</b></div>
        <div class="col-sm-10"> <%=row.get("adddate") == null ? "" : row.get("adddate")%> </div>
      </div>
      <div class="row">
        <div class="col-sm-2 text-right"><b>最后登录时间</b></div>
        <div class="col-sm-10"> <%=row.get("lastdate") == null ? "" : row.get("lastdate")%> </div>
      </div>
      <div class="row">
        <div class="col-sm-2 text-right"><b>登录ip</b></div>
        <div class="col-sm-10"> <%=row.get("last_ip") == null ? "" : row.get("last_ip")%> </div>
      </div>
      <div class="row">
        <div class="col-sm-2 text-right"><b>登录次数</b></div>
        <div class="col-sm-10"> <%=row.get("login_num") == null ? "" : row.get("login_num")%> </div>
      </div>
    </div>
    <div class="col-xs-2">
      <h4>快捷链接</h4>
      <ul class="nav nav-pills nav-stacked">
        <li role="presentation"><a href="/user/login.htm">修改密码</a></li>
		<li role="presentation"><a href="/user/index.htm">最新用户</a></li>
      </ul>
    </div>
  </div>
  
  <jsp:include page="../common/footer.jsp" />
