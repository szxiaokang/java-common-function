<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<script type="text/javascript">
var url = "<%=(request.getAttribute("url") == null || "".equals(request.getAttribute("url"))) ? "/" : request.getAttribute("url")%>";
location.href = url;
</script>
