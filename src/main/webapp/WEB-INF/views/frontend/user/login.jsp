<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage="" %>
<jsp:include page="../common/header.jsp" />
  
  <div class="row" style="margin-top:20px;">
    <div class="col-xs-1"></div>
    <div class="col-xs-6">
		<h4>登录</h4>
     	<section class="content">
		<form id="loginForm" action="" method="post" class="form-horizontal">
			<input type="hidden" name="action" id="action" value="" /> 
			<input type="hidden" name="url" id="url" value="<%=request.getAttribute("url")%>" />
			<div class="form-group">
				<label for="Email" class="col-sm-2 control-label">用户名</label>
				<div class="col-sm-9">
					<input name="username" type="text" class="form-control" value="" id="username" placeholder="username">
				</div>
			</div>

			<div class="form-group">
				<label for="password" class="col-sm-2 control-label">密码</label>
				<div class="col-sm-9">
					<input name="password" type="password" value="" class="form-control" id="password" placeholder="密码">
				</div>
			</div>

			<div class="form-group">
				<label for="captch" class="col-sm-2 control-label">验证码</label>
				<div class="col-sm-7">
					<input name="captch" type="text" class="form-control" id="captch" placeholder="验证码">
				</div>
				<div style="background:none; cursor:pointer" id="refresh_captch"><img alt="验证码" title="点击刷新" id="captch_img" src="/captch.htm" /> </div>
				
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
		  <li role="presentation"><a href="/register/index.htm">注册</a></li>
		  <li role="presentation"><a href="/user/index.htm">最新用户</a></li>
		</ul>	
	</div>
  </div>
  
  <script type="text/javascript">
	$(function() {
		$('#refresh_captch').click(function() {
			$('#captch_img').attr('src', '/captch.htm?rand=' + Math.random());
		});

		$('#loginForm').bootstrapValidator({
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				username : {
					validators : {
						notEmpty : {
							message : '用户名不能为空'
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
				captch : {
					validators : {
						notEmpty : {
							message : '验证码不能为空'
						},
						stringLength : {
							min : 4,
							max : 4,
							message : '验证码为4位数字或字母'
						}
					}
				}
			}
		}).on('success.form.bv', function(e) {
			e.preventDefault();
			
			//提交
			$('#action').val('login');
			$('#msg').hide();
			$.post('/login/index.htm', $('#loginForm').serialize(), function(data) {
				if (data.status == 0) {
					msgSuccess('登录成功, 正在跳转', '/user/main.htm');
				} else if (data.status == -1) {
					msgFail('提交的参数不正确');
					$('#refresh_captch').click();
				} else if (data.status == -2) {
					msgFail('验证码不正确');
				} else if (data.status == -3) {
					msgFail('提交的参数不能为空');
				} else if (data.status == -4) {
					msgFail('用户名格式不正确');
				} else if (data.status == -5) {
					msgFail('用户名或密码错误');
				} else {
					msgFail('登录失败, 请稍后再试');
				}
			}, 'json');
				
		});
	})
</script>
  
<jsp:include page="../common/footer.jsp" />
