<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="../common/header.jsp" />

<aside class="right-side">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h3>
			新闻管理 <small><a class="btn btn-info btn-sm" href="/admin/news/add.htm">添加新闻</a> </small>
		</h3>
	</section>
	<!-- Main content -->
	<section class="content">
		<form id="searchForm" action="" method="get" class="form-inline">
			<div class="input-group">
				<select name="type" id="type" class="form-control">
              <option value="">分类搜索</option>
              <%
			HashMap<String, String> typeMaps = new HashMap<String, String>();
			List<Map<String, Object>>  types = (List) request.getAttribute("types");
			String searchType = request.getParameter("type") != null && !"".equals(request.getParameter("type")) ? request.getParameter("type").toString() : "";
			int size = types.size();
			for(int i = 0; i < size; i++){ 
				typeMaps.put(types.get(i).get("id").toString(), types.get(i).get("name").toString());
				out.print("<option value=\""+ types.get(i).get("id") +"\"");
				if (searchType.equals(types.get(i).get("id").toString())) {
					out.print(" selected");
				}
				out.print(">");
				out.print(types.get(i).get("name"));
				out.println("</option>");
			}
			%>
            </select>
			</div>
			<div class="input-group">
				<input type="text" name="search" id="search" placeholder="标题" value="<%=request.getParameter("search") == null ? "" : request.getParameter("search") %>" class="form-control input-sm"> <span class="input-group-btn">
					<button type="submit" class="btn btn-default btn-sm">
						<i class="glyphicon glyphicon-search"></i>
					</button> </span>
			</div>
			<div id="msg"></div>
		</form>
		<form id="listForm" action="" method="post">
			<input type="hidden" name="action" id="action" value="" />
			<input type="hidden" name="url" id="url" value="<%=request.getAttribute("url")%>" />
			<div class="view" style="margin-top:20px">
				<table width="100%" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th width="5%">&nbsp;</th>
							<th width="5%">编号</th>
							<th width="11%">分类</th>
							<th width="50%">标题</th>
							<th width="12%">添加</th>
							<th width="11%">修改</th>
							<th width="6%">操作</th>
						</tr>
					</thead>
					<tbody>
						<%
							List<Map<String, Object>>  rows = (List) request.getAttribute("rows");
							if (rows != null) {
								size = rows.size();
								for(int i = 0; i < size; i++){
						%>
						<tr>
							<td><input type="checkbox" name="ids" value="<%=rows.get(i).get("id")%>" />							</td>
							<td><%=rows.get(i).get("id")%></td>
							<td><%=rows.get(i).get("type") != null && !"".equals(rows.get(i).get("type")) ? typeMaps.get(rows.get(i).get("type").toString()) : "-"%></td>
							<td>
							<%
							if (rows.get(i).get("images") != null && !"".equals(rows.get(i).get("images"))) {
								out.print("<a href=\""+ rows.get(i).get("images") +"\" target=\"_blank\"><span class=\"glyphicon glyphicon-picture\" title=\"有新闻图片\"></span></a>");
							}
							%>
							<%=rows.get(i).get("title")%></td>
							<td><%=rows.get(i).get("addtime")%></td>
							<td><%=rows.get(i).get("edittime")%></td>
							<td><a href="add.htm?action=edit&id=<%=rows.get(i).get("id")%>&url=<%=request.getAttribute("url")%>">修改</a></td>
						</tr>
						<%
							}
						              }
						%>
					</tbody>
				</table>
			</div>
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-6 col-md-4">
						<div class="form-group">
							<label for="_ids"> <input type="checkbox" name="_ids" id="_ids" title="select all" onClick="selectBox(this, 'ids')"> 全选</label> &nbsp;&nbsp;
							<button type="button" class="btn btn-danger btn-sm" onClick="batchDelete('listForm', 'ids', 'delete')">删除</button>
						</div>
					</div>
					<div class="col-xs-12 col-sm-6 col-md-8">
					<nav>
						<%=request.getAttribute("paging")%>
						</nav>
					</div>
				</div>
			</div>
		</form>
	</section>
	<!-- /.content -->
</aside>


<jsp:include page="../common/footer.jsp" />
