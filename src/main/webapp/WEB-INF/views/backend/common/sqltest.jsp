<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SQL 测试</title>
<style type="text/css">
<!--
body,td,th {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 14px;
	color: #555555;
}
body {
	background-color: #FDFDFD;
	margin-left: 10px;
	margin-top: 10px;
	margin-right: 10px;
	margin-bottom: 10px;
}
-->
</style></head>

<body>
<h1>SQL 防注入测试 :)</h1><hr>
<form action="" method="get">
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="12%" height="67" align="right"><b style="font-size:16px">关键字</b>:</td>
    <td width="88%"><input name="search" type="text" id="search" style="padding:10px" /></td>
  </tr>
  <tr>
    <td height="67" align="right">&nbsp;</td>
    <td><input type="submit" name="Submit" value="提交" /></td>
  </tr>
</table>
</form>
<h2>结果:</h2>
<div style="padding:10px; font-size:16px; background:#fff">
 <%
if (request.getParameter("search") == null) {
	out.print("提交关键字才有 :)");
} else {
	out.print(request.getAttribute("sql"));
}

%>
</div>

</body>
</html>
