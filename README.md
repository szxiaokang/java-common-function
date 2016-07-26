# java maven example
maven 测试程序(本例所需jar包已放入lib下, 也可以不用maven)

# 功能说明


1 前端 用户注册/登录/新闻列表/详情/最新会员等

2 后端 管理员登录/简略统计/添加/管理管理员/ 新闻添加/编辑/管理等

# 部署说明

此程序需要以下环境支持:
mysql5.x
jdk 1.7 及以上
tomcat 7
memcache
redis


1 新建数据库 mydatabase, 将mydatabase.sql导入

2 更改src/main/resources/config.properties 的数据库连接信息:ip/username/password 等

3 在hosts里绑定域名 127.0.0.1 www.blog.com

4 tomcat7 中增加配置:
	<Host name="www.blog.com"  appBase="" unpackWARs="true" autoDeploy="true">
		<Context path="" docBase="E:/dev-www/java/maven_example/src/main/webapp" reloadable="true"/>  
    </Host>

5 重启tomcat, 在浏览器里输入 http://www.blog.com:8080/index.htm
	祝你好运 :)
	如有疑问, 可联系 273030282@qq.com