<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage="" %>
<jsp:include page="../common/header.jsp" />
  <div class="row" style="margin-top:20px;">
    <div class="col-xs-1"></div>
    <div class="col-xs-6">
      <h4>最新注册用户</h4>
      <table width="100%" class="table table-striped table-hover" style="margin-top:25px">
        <tbody>
          <%
					List<Map<String, Object>> rows = (List) request.getAttribute("rows");
					int size = rows.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
				%>
          <tr>
            <td><%=rows.get(i).get("email")%></td>
            <td><%=rows.get(i).get("addtime")%></td>
          </tr>
          <%
					}
					} else {
						out.println("<tr><td colspan=\"2\">空空如也...</td></tr>");
					}
				%>
        </tbody>
      </table>
    </div>
    <div class="col-xs-2">
      <h4>快捷链接</h4>
      <ul class="nav nav-pills nav-stacked">
        <li role="presentation"><a href="/user/register.htm">注册</a></li>
        <li role="presentation"><a href="/user/login.htm">登录</a></li>
      </ul>
    </div>
  </div>
  <jsp:include page="../common/footer.jsp" />
