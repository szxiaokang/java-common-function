<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage=""%>
<jsp:include page="../common/header.jsp" />
<div class="row" style="margin-top:20px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-8">
		<h4>最新新闻</h4>
		<table width="100%" class="table table-striped table-hover">
			<tbody>
				<%
					List<Map<String, Object>> rows = (List) request.getAttribute("rows");
					int size = rows.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
				%>
				<tr>
					<td>
					<a href="/detail.htm?id=<%=rows.get(i).get("id") %>"><%=rows.get(i).get("images") != null && !"".equals(rows.get(i).get("images")) ? "<span class=\"glyphicon glyphicon-picture\"></span>" : "" %> <%=rows.get(i).get("title")%></a>
						</td>
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
		<h4>新闻分类</h4>
		<ul class="nav nav-pills nav-stacked">
			<li role="presentation"<%=request.getParameter("type") == null || "".equals(request.getParameter("type")) ? " class=\"active\"" : "" %>><a href="/index.htm">所有</a>
			<%
			HashMap<String, String> typeMaps = new HashMap<String, String>();
			List<Map<String, Object>>  types = (List) request.getAttribute("types");
			String searchType = request.getParameter("type") != null && !"".equals(request.getParameter("type")) ? request.getParameter("type").toString() : "";
			size = types.size();
			for(int i = 0; i < size; i++){ 
				typeMaps.put(types.get(i).get("id").toString(), types.get(i).get("name").toString());
				out.print("<li role=\"presentation\"");
				if (searchType.equals(types.get(i).get("id").toString())) {
					out.print(" class=\"active\"");
				}
				out.print("><a href=\"/index.htm?type="+ types.get(i).get("id") +"\">" + types.get(i).get("name") + "</a>");
			}
			 %>
		</ul>
	</div>
</div>
<jsp:include page="../common/footer.jsp" />
