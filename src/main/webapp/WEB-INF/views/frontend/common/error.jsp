<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="header.jsp" />
  <aside class="right-side">
    <section class="alert alert-danger"> 错误信息提示 </section>
    <section class="content">
      <div>
        <h4> <%=request.getAttribute("msg") == null ? "参数不正确" : request.getAttribute("msg")%>, 请检查 :) <small><a class="btn btn-info" href="<%=request.getAttribute("url") == null ? "javascript:history.go(-1)" : request.getAttribute("url") %>">返 回</a> </small></h4>
      </div>
    </section>
  </aside>
  <jsp:include page="footer.jsp" />
