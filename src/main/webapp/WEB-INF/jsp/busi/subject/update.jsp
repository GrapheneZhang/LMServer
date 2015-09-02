<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/messages_zh.js"></script>
<script type="text/javascript">
$(function(){
    $("#form").validate({
        rules:{
            enName:{
                required:true,
                remote:{
                    url:"${pageContext.request.contextPath}/subject/query/check",
                    type:"post",
                    data:{id:$("#id").val()}
                }
            },
            zhName:{
            	required:true,
                remote:{
                    url:"${pageContext.request.contextPath}/subject/query/check",
                    type:"post",
                    data:{id:$("#id").val()}
                }
            }
        }, messages: {
        	enName:{
                remote:"此英文名已存在,请重新输入!"
            },
            zhName:{
                remote:"此中文名已存在,请重新输入!"
            }
        }, submitHandler:function(form){
        	$(form).ajaxSubmit({
                success:function(data){
                	if(data.state==200){
                		$('#mainGrid').datagrid("reload");
                		$.messager.alert("消息提示", "成功！");
                        $('#div_cu').dialog('close');
                	}else{
                		$.messager.alert("消息提示", "失败！"+data.errorMsg);
                	}
                }
            });
        }
    });
});
</script>
<!-- 新增页面 -->
<sp:form id="form" method="post" commandName="subject" action="${pageContext.request.contextPath}/subject/update">
    <table>
        <tr>
            <td>英文名:</td>
            <td>
                <sp:input type="hidden" path="id" id="id" />
                <sp:input type="text" path="enName" id="enName" />
            </td>
        </tr>
        <tr>
            <td>中文名:</td>
            <td>
                <sp:input type="text" path="zhName" id="zhName" />
            </td>
        </tr>
    </table>
    <div style="text-align:center;padding:5px">
        <input type="submit" value="确定"/>
	</div>
</sp:form>