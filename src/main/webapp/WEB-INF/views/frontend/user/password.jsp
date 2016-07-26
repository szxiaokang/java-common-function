<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage="" %>
<jsp:include page="../common/header.jsp" />
  	<%
HashMap<String, Object> row = new HashMap<String, Object>();
if (request.getAttribute("row") != null) {
	row = (HashMap) request.getAttribute("row");
}

%>
  
  <div class="row" style="margin-top:20px;">
    <div class="col-xs-1"></div>
    <div class="col-xs-6">
		<h4>注册</h4>
     	
		<section class="content">
		<form id="passwordForm" action="" method="post" class="form-horizontal">
			<input type="hidden" name="action" id="action" value="<%=request.getAttribute("action") == null ? "add" : request.getAttribute("action")%>" /> 
			<div class="form-group">
				<label for="Email" class="col-sm-2 control-label">原密码</label>
				<div class="col-sm-10">
					<input name="oldpassword" type="password" class="form-control" value="<%=row.get("email") == null ? "" : row.get("email")%>" id="oldpassword" placeholder="原密码">
				</div>
			</div>

			<div class="form-group">
				<label for="password" class="col-sm-2 control-label">密码</label>
				<div class="col-sm-10">
					<input name="password" type="password" value="<%=row.get("adminid") == null ? "" : "######"%>" class="form-control" id="password" placeholder="密码">
				</div>
			</div>

			<div class="form-group">
				<label for="repassword" class="col-sm-2 control-label">确认密码</label>
				<div class="col-sm-10">
					<input name="repassword" type="password" value="<%=row.get("adminid") == null ? "" : "######"%>" class="form-control" id="repassword" placeholder="确认密码">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">提交</button>
				</div>
			</div>

			<div id="msg">
				
			</div>


		</form>
	</section>
		
		
    </div>
    <div class="col-xs-2">
		<h4>快捷链接</h4>
		<ul class="nav nav-pills nav-stacked">
		  <li role="presentation"><a href="/user/main.htm">基本信息</a></li>
		  <li role="presentation"><a href="/user/index.htm">最新用户</a></li>
		</ul>	
	</div>
  </div>
  

<script type="text/javascript">
	$(function() {
		$('#passwordForm').bootstrapValidator({
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				oldpassword : {
					validators : {
						notEmpty : {
							message : '原密码不能为空'
						},
						callback: {
							message: '原密码不正确'
						}
					}
				},
				password : {
					validators : {
						notEmpty : {
							message : '密码不能为空'
						},
						stringLength : {
							min : 6,
							max : 20,
							message : '6-20个字符'
						}
					}
				},
				repassword : {
					validators : {
						notEmpty : {
							message : '确认密码不能为空'
						},
						identical : {
							field : 'password',
							message : '您两次输入的密码不一致'
						}
					}
				}
			}
		}).on('success.form.bv', function(e) {
			e.preventDefault();
			
			//提交
			$('#action').val('password');
			$('#msg').hide();
			$.post('/user/password.htm', $('#passwordForm').serialize(), function(data) {
				if (data.status == 0) {
					msgSuccess('修改成功', '/user/main.htm');
				} else if (data.status == -1) {
					msgFail('提交的参数不正确');
					$('#refresh_captch').click();
				} else if (data.status == -2) {
					msgFail('提交的参数不能为空');
				} else if (data.status == -3) {
					msgFail('两次输入的密码不一致');
				} else if (data.status == -4) {
					msgFail('原密码不正确');
				} else if (data.status == -5) {
					msgFail('更新失败, 请稍后再试!');
				} else {
					msgFail('注册失败, 请稍后再试');
					return false;
				}
			}, 'json');
				
		});
	})
</script>  
  
  
  
<jsp:include page="../common/footer.jsp" />