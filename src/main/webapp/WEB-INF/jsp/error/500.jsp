<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/commons/jquery-1.9.1.min.js"></script>
<title>服务器错误</title>
<script type="text/javascript">
$(function(){
    var count=0;
    var stop=setInterval(function(){
        $("#message").text("抱歉..服务器正在维护或出现未知的异常，请稍后再试。"+parseInt(5-count)+"秒后将返回首页。");
        count++;
        if(count==6){
            clearInterval(stop);//停止倒计时
            location.href="<%=request.getContextPath()%>/login";
        }
    },1000);
});
</script>
</head>
<body>
    <span id="message">抱歉..服务器正在维护或出现未知的异常，请稍后再试。5秒后将返回首页。</span>
</body>
</html>