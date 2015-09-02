<%@ page pageEncoding="UTF-8"%>
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
                    type:"post"
                }
            },
            zhName:{
            	required:true,
                remote:{
                    url:"${pageContext.request.contextPath}/subject/query/check",
                    type:"post"
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
<form id="form" method="post" action="${pageContext.request.contextPath}/subject/add">
    <table>
        <tr>
            <td>英文名:</td>
            <td>
                <input type="text" name="enName" id="enName" />
            </td>
        </tr>
        <tr>
            <td>中文名:</td>
            <td>
                <input type="text" name="zhName" id="zhName" />
            </td>
        </tr>
    </table>
    <div style="text-align:center;padding:5px">
        <input type="submit" value="确定"/>
        <input type="reset" value="重置"/>
	</div>
</form>