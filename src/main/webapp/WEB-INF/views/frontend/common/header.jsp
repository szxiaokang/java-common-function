<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage="" %>
<%@ page language="java" import="com.web.util.Util" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href="/assets/css/bootstrap.min.css" rel="stylesheet" />
<link href="/assets/css/bootstrap-theme.min.css" rel="stylesheet" />
<link href="/assets/css/bootstrapValidator.min.css" rel="stylesheet" />
<link href="/assets/css/jquery-ui.min.css" rel="stylesheet" />

<script type="text/javascript" src="/assets/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/assets/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="/assets/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/assets/js/front.js"></script>
<title>JAVA NEWS - <%=request.getAttribute("title") != null ? request.getAttribute("title") : ""%></title>
</head>
<body>
<div id="alert_message" style="display:none"></div>
<div class="container-fluid" style="margin-top:10px">
  <div class="row">

    <div class="col-xs-1"></div>
    <div class="col-xs-10" style="padding-top:15px">
      <ul class="nav nav-tabs">
	  					<%
						String uri = request.getRequestURI();
					%>
        <li role="presentation"<%=uri.indexOf("index/index") != -1 || uri.indexOf("index/detail") != -1 ? " class=\"active\"" : ""%>><a href="/index.htm">
          <h4>新闻</h4>
          </a></li>
        <li role="presentation"<%=uri.indexOf("user") != -1 ? " class=\"active\"" : ""%>><a href="/user/index.htm">
          <h4>用户</h4>
          </a></li>
       
        <li role="presentation"<%=uri.indexOf("about") != -1 ? " class=\"active\"" : ""%>><a href="/about.htm">
         <h4>关于</h4>
          </a></li>
       
	   
		 <li role="presentation" class="" style="float: right; margin-top:35px; padding-right:45px">
		 <%
	   Map<String, Object> userSess = new HashMap<String, Object>();
	   userSess = Util.getSession(Util.getConfig("cookie.key"), request);
	   if (userSess != null) {
	   	
	   %>
			  <span onClick="location.href='/user/main.htm'" style="cursor:pointer; color:#FF6600"><i class="glyphicon glyphicon-user"></i> <%=userSess.get("email")%></span>, 欢迎回来! 
			 
			 <span style="padding-right:20px;margin-left:20px; cursor:pointer; color:#0066FF" onClick="logout()">
				<i class="glyphicon glyphicon-log-out"></i> <span>退出</span>
			 </span>
		<%
		} else {
		%>
		<span onClick="location.href='/user/login.htm'" style="cursor:pointer; color:#FF6600"><i class="glyphicon glyphicon-log-in"></i> 登录</span>
		<span onClick="location.href='/user/register.htm'" style="cursor:pointer; color:#0066CC; margin-left:15px; margin-right:30px;"><i class="glyphicon glyphicon-edit"></i> 注册</span>
		<%
		}
		%>
		 </li>

		
      </ul>
    </div>
    <div class="col-xs-1"><img style="height:80px; position:absolute;right:135px; z-index:-1" src="/assets/images/logo.gif"></div>
  </div>
