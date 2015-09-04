<%@ page pageEncoding="UTF-8"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/messages_zh.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/commons/ajaxfileupload.js"></script>
<script type="text/javascript">
$(function(){
    $("#form").validate({
        rules:{
        	excelFile:{
                required:true
            }
        }, messages: {
        	excelFile:{
        		required:"请选择一个excel文件!",
            }
        }, submitHandler:function(form){
        	$.ajaxFileUpload({
                url:"${pageContext.request.contextPath}/lmuser/add/excel",
                type: "POST",
                secureuri: false,
                fileElementId: 'excelFile',
                dataType: "json",
                success: function (data) {
                	if(data.state==200){
                        $('#mainGrid').datagrid("reload");
                        $.messager.alert("消息提示", data.correctMsg);
                        $('#div_cu').dialog('close');
                    }else{
                        $.messager.alert("消息提示", "失败！"+data.errorMsg);
                    }
                },error: function(data,status,e){
                	$.messager.alert("消息提示", "失败！文件太大，请选择10MB以下的文件！");
                }
            });
        }
    });
});
</script>
<!-- 新增页面 -->
<form id="form" method="post" action="" enctype="multipart/form-data">
    <table>
        <tr>
            <td colspan="2">请选择基于“雷鸣用户Excel导入.xls”文件的文件(不能大于10MB)</td>
        </tr>
        <tr>
            <td>Excel文件:</td>
            <td>
                <input type="file" name="excelFile" id="excelFile" accept="application/vnd.ms-excel" />
            </td>
        </tr>
    </table>
    <div style="text-align:center;padding:5px">
        <input type="submit" value="上传"/>
	</div>
</form>