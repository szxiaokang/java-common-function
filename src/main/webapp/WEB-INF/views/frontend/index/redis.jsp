<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage=""%>
<jsp:include page="../common/header.jsp" />

<div class="row" style="margin-top:20px;">
	<div class="col-xs-1"></div>
	<div class="col-xs-7">

		<div style="border:solid 1px #efefef; padding:20px; line-height:24px; margin-bottom:20px">

			<form action="" method="post" class="form-horizontal">
				<div class="form-group">
					<label for="key" class="col-sm-2 control-label">Key</label>
					<div class="col-sm-10">
						<input name="key" type="text" class="form-control" value="<%=request.getAttribute("key") != null ? request.getAttribute("key") : "" %>" id="key" placeholder="key">
					</div>
				</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">提交</button>
				</div>
			</div>
				
				<div class="form-group">
					<label for="key" class="col-sm-2 control-label">Value</label>
					<div class="col-sm-10">
						<h3><%=request.getAttribute("value") %></h3>
					</div>
				</div>
			</form>

		</div>
	</div>
	<div class="col-xs-3">
		
	</div>
</div>
<jsp:include page="../common/footer.jsp" />
