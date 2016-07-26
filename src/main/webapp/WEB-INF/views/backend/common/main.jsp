<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="header.jsp" />
<%
Map<String, Object> data = (Map) request.getAttribute("data");
System.out.println("Data: " + data);
%>
<aside class="right-side">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h3>
			简略统计
		</h3>
	</section>
	<!-- Main content -->
	<section class="content">
		
		<div class="row">
        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-aqua"><i class="glyphicon glyphicon-user"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">用户数量</span>
              <span class="info-box-number"><%=data.get("user") %></span>
            </div>
            <!-- /.info-box-content -->
          </div>
          <!-- /.info-box -->
        </div>
        <!-- /.col -->
        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-red"><i class="glyphicon glyphicon-globe"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">新闻总数</span>
              <span class="info-box-number"><%=data.get("news") %></span>
            </div>
            <!-- /.info-box-content -->
          </div>
          <!-- /.info-box -->
        </div>
        <!-- /.col -->

        <!-- fix for small devices only -->
<div class="clearfix visible-sm-block"></div>

        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-teal"><i class="glyphicon glyphicon-circle-arrow-down"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">新闻总点击量</span>
              <span class="info-box-number"><%=data.get("click") %></span>
            </div>
            <!-- /.info-box-content -->
          </div>
          <!-- /.info-box -->
        </div>
   
	  

        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-orange"><i class="glyphicon glyphicon-circle-arrow-down"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">管理员</span>
              <span class="info-box-number"><%=data.get("admin") %></span>
            </div>
            <!-- /.info-box-content -->
          </div>
          <!-- /.info-box -->
        </div>
      </div>
	  
	  
	  <!--table-->
	  
	  
	  <div class="row" style="margin-top:30px">
	  	<div class="col-md-6">
			<h4>最新注册用户</h4>
			<table class="table table-striped table-hover">
			<thead>
				<tr>
					<td>编号</td>
					<td>用户名</td>
					<td>注册时间</td>
				</tr>
			</thead>
			<tbody>	
				<%
				List<Map<String, Object>> rows = (List) data.get("userRows");
				int len = rows.size();
				if (rows != null && len > 0) {
					for (int i = 0; i < len; i++) {
				
				 %>
				<tr>
					<td><%=rows.get(i).get("uid") %></td>
					<td><%=rows.get(i).get("email") %></td>
					<td><%=rows.get(i).get("addtime") %></td>
				</tr>
				<%
					}
				} else {
					out.println("<tr><td colspan=\"3\">空空如也...</td></tr>");
				}
				 %>
			</tbody>				
			</table>
		</div>
		<div class="col-md-6">
			<h4>新闻点击排行</h4>
			<table class="table table-striped table-hover">
				<thead>
				<tr>
					<td>编号</td>
					<td>标题</td>
					<td>点击量</td>
				</tr>
				</thead>
				<tbody>
				<%
				rows = (List) data.get("newsRows");
				len = rows.size();
				if (rows != null && len > 0) {
					for (int i = 0; i < len; i++) {
				
				 %>
				<tr>
					<td><%=rows.get(i).get("id") %></td>
					<td><%=rows.get(i).get("title") %></td>
					<td><%=rows.get(i).get("click") %></td>
				</tr>
				<%
					}
				} else {
					out.println("<tr><td colspan=\"3\">空空如也...</td></tr>");
				}
				 %>	
				 </tbody>		
			</table>
		</div>
		
		
	  </div>
	  
	</section>
	<!-- /.content -->
</aside>


<jsp:include page="footer.jsp" />
