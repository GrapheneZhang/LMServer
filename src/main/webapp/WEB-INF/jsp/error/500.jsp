<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务器错误</title>
<script type="text/javascript">
setInterval(function(){
	location.href="<%=request.getContextPath()%>/login";
}, 5000)
</script>
</head>
<body>
服务器正在维护或出现不可以的异常，请稍后再试<br/>
5秒后将跳转到主页
</body>
</html>