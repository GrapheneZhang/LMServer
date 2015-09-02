<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/additional-methods-lm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/messages_zh.js"></script>
<script type="text/javascript">
$(function(){
    $("#form").validate({
        rules:{
        	content:{
                required:true,
                maxlength:5000
            },
            answer:{
            	maxlength:5000
            },
            subject:{
            	required:true
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
<sp:form id="form" commandName="question" method="post" action="${pageContext.request.contextPath}/question/add">
    <table>
        <tr>
            <td>题目内容:</td>
            <td>
                <textarea name="content" id="content"></textarea>
            </td>
        </tr>
        <tr>
            <td>答案:</td>
            <td>
                <textarea name="answer" id="answer"></textarea>
            </td>
        </tr>
        <tr>
            <td>所属科目:</td>
            <td>
                <sp:select path="subject" items="${list}" itemValue="id" itemLabel="zhName"/>
            </td>
        </tr>
    </table>
    <div style="text-align:center;padding:5px">
        <input type="submit" value="确定"/>
        <input type="reset" value="重置"/>
	</div>
</sp:form>