<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="../common/header.jsp" />


<aside class="right-side">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h3>
			管理员列表 <small><a class="btn btn-info btn-sm" href="/admin/admin/add.htm">添加管理员</a> </small>
		</h3>
	</section>
	<!-- Main content -->
	<section class="content">
		<form id="searchForm" action="" method="get" class="form-inline">
			<div class="input-group">
				<input type="text" name="search" id="search" placeholder="用户名或Email" value="<%=request.getParameter("search") == null ? "" : request.getParameter("search") %>" class="form-control input-sm"> <span class="input-group-btn">
					<button type="submit" class="btn btn-default btn-sm">
						<i class="glyphicon glyphicon-search"></i>
					</button> </span>
			</div>
			<div id="msg"></div>
		</form>
		<form id="listAdmin" action="" method="post">
			<input type="hidden" name="action" id="action" value="" />
			<input type="hidden" name="url" id="url" value="<%=request.getAttribute("url")%>" />
			<div class="view" style="margin-top:20px">
				<table width="100%" class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>&nbsp;</th>
							<th>编号</th>
							<th>用户名</th>
							<th>Email</th>
							<th>添加</th>
							<th>修改</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<%
							List<Map<String, Object>>  rows = (List) request.getAttribute("rows");
							if (rows != null) {
								int size = rows.size();
								for(int i = 0; i < size; i++){
						%>
						<tr>
							<td><input type="checkbox" name="ids" value="<%=rows.get(i).get("adminid")%>" />
							</td>
							<td><%=rows.get(i).get("adminid")%></td>
							<td><%=rows.get(i).get("username")%></td>
							<td><%=rows.get(i).get("email")%></td>
							<td><%=rows.get(i).get("addtime")%></td>
							<td><%=rows.get(i).get("edittime")%></td>
							<td><a href="add.htm?action=edit&id=<%=rows.get(i).get("adminid")%>&url=<%=request.getAttribute("url")%>">修改</a></td>
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
							<button type="button" class="btn btn-danger btn-sm" onClick="batchDelete('listAdmin', 'ids', 'delete')">删除</button>
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
