<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage=""%>
<jsp:include page="../common/header.jsp" />
<%
	Map<String, Object> row = (Map) request.getAttribute("row");
%>

<div class="row" style="margin-top:20px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-7">
		<h4 class="text-center"><%=row.get("title")%> <small><%=row.get("adddate")%></small>
		</h4>
		<%
			if (row.get("images") != null && !"".equals(row.get("images"))) {
		%>
		<p style="padding:5px;">
			<img src="<%=row.get("images")%>" />
		</p>
		<%
			}
		%>
		<div style="border:solid 1px #efefef; padding:20px; line-height:24px; margin-bottom:20px">
		
		<%=row.get("content")%>
		
		</div>
	</div>
	<div class="col-xs-3">
		<h4>相关新闻</h4>
		<table width="100%" class="table table-striped table-hover">
			<tbody>
				<%
					List<Map<String, Object>> rows = (List) request.getAttribute("rows");
					int size = rows.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
				%>
				<tr>
					<td><a href="/detail.htm?id=<%=rows.get(i).get("id")%>"><%=rows.get(i).get("images") != null && !"".equals(rows.get(i).get("images")) ? "<span class=\"glyphicon glyphicon-picture\"></span>" : ""%> <%=rows.get(i).get("title")%></a>
					</td>
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
</div>
<jsp:include page="../common/footer.jsp" />
