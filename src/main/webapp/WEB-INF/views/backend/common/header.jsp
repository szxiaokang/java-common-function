<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.web.util.Util"%><!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Admin</title>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<link href="/assets/css/bootstrap3.3.5.css" rel="stylesheet">
<link href="/assets/css/bootstrapValidator.min.css" rel="stylesheet">
<link href="/assets/css/jquery-ui.min.css" rel="stylesheet">
<link href="/assets/css/AdminLTE.css" rel="stylesheet">
<script src="/assets/js/jquery-1.11.3.min.js"></script>
<script src="/assets/js/bootstrap.min.js"></script>
<script src="/assets/js/bootstrapValidator.min.js"></script>
<script src="/assets/js/jquery-ui.min.js"></script>
<script src="/assets/js/admin.js"></script>
<script type="text/javascript">
	$(function() {
		$(".sidebar .treeview").tree();
		
		    $("[data-toggle='offcanvas']").click(function(e) {
        e.preventDefault();

        //If window is small enough, enable sidebar push menu
        if ($(window).width() <= 992) {
            $('.row-offcanvas').toggleClass('active');
            $('.left-side').removeClass("collapse-left");
            $(".right-side").removeClass("strech");
            $('.row-offcanvas').toggleClass("relative");
        } else {
            //Else, enable content streching
            $('.left-side').toggleClass("collapse-left");
            $(".right-side").toggleClass("strech");
        }
    });
		
	})
</script>
</head>
<body class="skin-blue">
	<div id="alert_message" style="display:none"></div>
	<!-- header logo: style can be found in header.less -->
	<header class="header">
		<a href="#" class="logo">系统管理</a>
		<!-- Header Navbar: style can be found in header.less -->
		<nav class="navbar navbar-static-top" role="navigation">
			<!-- Sidebar toggle button-->
			<a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </a>
						
			<div class="navbar-right">
				<ul class="nav navbar-nav">
					<!-- User Account: style can be found in dropdown.less -->
					<li><a href="javascript:void(0)"> <i class="glyphicon glyphicon-user"></i> <span>
					<%
					Map<String, Object> adminSession = Util.getSession(Util.getConfig("cookie.admin.key"), request);
					out.println(adminSession.get("username"));
					 %>
					, 欢迎回来! </span></a></li>
					<li><a href="javascript:logout()" > <i class="glyphicon glyphicon-log-out"></i> <span>退出</span></a></li>
				</ul>
			</div>
		</nav>
	</header>
	<div class="wrapper row-offcanvas row-offcanvas-left">
		<!-- Left side column. contains the logo and sidebar -->
		<aside class="left-side sidebar-offcanvas">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar">
				<!-- sidebar menu: : style can be found in sidebar.less -->
				<ul class="sidebar-menu">
					<%
						String uri = request.getRequestURI();
					%>
					<li class="treeview <%=uri.indexOf("news") != -1 ? "active" : ""%>"><a href="#"> <i class="glyphicon glyphicon-globe"></i> <span>新闻</span> <i class="glyphicon glyphicon-menu-down pull-right"></i> </a>
						<ul class="treeview-menu">
							<li class="<%=uri.indexOf("news/add.jsp") != -1 ? "curr" : ""%>"><a href="/admin/news/add.htm"><i class="glyphicon glyphicon-menu-right"></i> 添加新闻</a>
							</li>
							<li class="<%=uri.indexOf("news/list.jsp") != -1 ? "curr" : ""%>"><a href="/admin/news/list.htm"><i class="glyphicon glyphicon-menu-right"></i> 新闻管理</a>
							<li class="<%=uri.indexOf("news/add_type.jsp") != -1 ? "curr" : ""%>"><a href="/admin/news/addType.htm"><i class="glyphicon glyphicon-menu-right"></i> 添加新闻类型</a>
							<li class="<%=uri.indexOf("news/list_type.jsp") != -1 ? "curr" : ""%>"><a href="/admin/news/listType.htm"><i class="glyphicon glyphicon-menu-right"></i> 新闻类型管理</a>
							</li>
						</ul>
					</li>
					<li class="treeview <%=uri.indexOf("admin") != -1 ? "active" : ""%>"><a href="#"> <i class="glyphicon glyphicon-glass"></i> <span>管理员</span> <i class="glyphicon glyphicon-menu-down pull-right"></i> </a>
						<ul class="treeview-menu ">
							<li class="<%=uri.indexOf("admin/add.jsp") != -1 ? "curr" : ""%>"><a href="/admin/admin/add.htm"><i class="glyphicon glyphicon-menu-right"></i> 添加管理员</a>
							</li>
							<li class="<%=uri.indexOf("admin/list.jsp") != -1 ? "curr" : ""%>"><a href="/admin/admin/list.htm"><i class="glyphicon glyphicon-menu-right"></i> 管理员列表</a>
							</li>
						</ul>
					</li>
					<li class="treeview <%=uri.indexOf("user") != -1 ? "active" : ""%>"><a href="#"> <i class="glyphicon glyphicon-user"></i> <span>用户管理</span> <i class="glyphicon glyphicon-menu-down pull-right"></i> </a>
						<ul class="treeview-menu">
							<li class="<%=uri.indexOf("user/list.jsp") != -1 ? "curr" : ""%>"><a href="/admin/user/list.htm"><i class="glyphicon glyphicon-menu-right"></i> 用户列表</a>
							</li>
						</ul>
					</li>
					<li class="<%=uri.indexOf("common/about.jsp") != -1 ? "active curr" : ""%>"><a href="/admin/about.htm"> <i class="glyphicon glyphicon-list-alt"></i> <span>说明</span> </a>
					</li>
					<li class="<%=uri.indexOf("common/main.jsp") != -1 ? "active curr" : ""%>"><a href="/admin/main.htm"> <i class="glyphicon glyphicon-list-alt"></i> <span>后台首页</span> </a>
					</li>
				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>