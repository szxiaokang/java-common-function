<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="../common/header.jsp" />
<aside class="right-side">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h3>
			添加管理员 <small><a class="btn btn-info btn-sm" href="/admin/admin/list.htm">管理员列表</a> </small>
		</h3>
	</section>
	<!-- Main content -->
	<%
HashMap<String, Object> row = new HashMap<String, Object>();
if (request.getAttribute("row") != null) {
	row = (HashMap) request.getAttribute("row");
}

%>
	<section class="content">
		<form id="addAdmin" action="" method="post" class="form-horizontal">
			<input type="hidden" name="action" id="action" value="<%=request.getAttribute("action") == null ? "add" : request.getAttribute("action")%>" /> 
			<input type="hidden" name="adminid" id="adminid" value="<%=row.get("adminid") == null ? "" : row.get("adminid")%>" />
			<input type="hidden" name="url" id="url" value="<%=request.getAttribute("url") == null ? "" : request.getAttribute("url")%>" />
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-2 control-label">用户名</label>
				<div class="col-sm-10">
					<input name="username" type="text" <%=row.get("adminid") == null ? "" : "disabled"%> value="<%=row.get("username") == null ? "" : row.get("username")%>" class="form-control" id="username" placeholder="用户名">
				</div>
			</div>

			<div class="form-group">
				<label for="inputPassword3" class="col-sm-2 control-label">密码</label>
				<div class="col-sm-10">
					<input name="password" type="password" value="<%=row.get("adminid") == null ? "" : "######"%>" class="form-control" id="password" placeholder="密码">
				</div>
			</div>

			<div class="form-group">
				<label for="inputPassword3" class="col-sm-2 control-label">确认密码</label>
				<div class="col-sm-10">
					<input name="repassword" type="password" value="<%=row.get("adminid") == null ? "" : "######"%>" class="form-control" id="repassword" placeholder="确认密码">
				</div>
			</div>

			<div class="form-group">
				<label for="inputPassword3" class="col-sm-2 control-label">Email</label>
				<div class="col-sm-10">
					<input name="email" type="email" class="form-control" value="<%=row.get("email") == null ? "" : row.get("email")%>" id="email" placeholder="Email">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">提交</button>
				</div>
			</div>

			<div id="msg">
				<%=row.get("adminid") == null ? "" : "<div class=\"alert alert-warning\" role=\"alert\">注意 : 不更改密码请勿输入</div>"%>
			</div>


		</form>
	</section>
	<!-- /.content -->
</aside>

<script type="text/javascript">
	$(function() {
		$('#addAdmin').bootstrapValidator({
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				username : {
					message : 'The username is not valid',
					validators : {
						notEmpty : {
							message : '用户名不能为空'
						},
						stringLength : {
							min : 4,
							max : 20,
							message : '4-20个字符'
						},
						regexp : {
							regexp : /^[a-zA-Z0-9_\.]+$/,
							message : '用户名必须是字符串或数字'
						},
						callback : {
							message : '此用户已经存在'
						}
					}
				},
				email : {
					validators : {
						notEmpty : {
							message : 'Email不能为空'
						},
						emailAddress : {
							message : '请输入正确的Email地址'
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
			//修改
			if ($('#adminid').val() != '' && $('#action').val() == 'save') {
				$.post('/admin/admin/add.htm', $('#addAdmin').serialize(), function(data) {
					if (data.status) {
						msgAlert('修改成功', $('#url').val());
					} else {
						msgFail('更新失败,请检查输入的内容');
					}
				}, 'json');
				return;
			}
			
			//添加
			$.post('/admin/admin/add.htm', {
				action : "validatorUsername",
				username : $('#username').val()
			}, function(data) {
				if (!data.status) {
					$('#addAdmin').data('bootstrapValidator').updateStatus('username', 'INVALID', 'callback');
					return false;
				} else {
					$('#action').val('add');
					$.post('/admin/admin/add.htm', $('#addAdmin').serialize(), function(data) {
						if (data.status) {
							msgSuccess('addAdmin', '添加成功');
						} else {
							msgFail('添加失败,请检查输入的内容或重新输入用户名');
						}
					}, 'json');
				}
			}, 'json');
		});
	})
</script>

<jsp:include page="../common/footer.jsp" />
