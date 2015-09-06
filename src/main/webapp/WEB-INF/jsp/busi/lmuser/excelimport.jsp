<%@ page pageEncoding="UTF-8"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/messages_zh.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/commons/ajaxfileupload.js"></script>
<script type="text/javascript">
function ajaxLoading(){
    var maskLevel=$(".window-mask").css("z-index")+1;
    $(".window-mask").css("z-index",maskLevel);
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({"z-index":maskLevel+1,display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
    
} 
function ajaxLoadEnd(){ 
     $(".datagrid-mask").remove(); 
     $(".datagrid-mask-msg").remove();
     $(".window-mask").remove();
}
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
        	ajaxLoading();//loding
        	if(true){//这个if是为了让前边的ajaxLoading一定执行
        		$.ajaxFileUpload({
                    url:"${pageContext.request.contextPath}/lmuser/add/excel",
                    type: "POST",
                    secureuri: false,
                    fileElementId: 'excelFile',
                    dataType: "json",
                    success: function (data) {
                        ajaxLoadEnd();//lodingEnd
                        if(data.state==200){
                            $('#mainGrid').datagrid("reload");
                            $.messager.alert("消息提示", data.correctMsg);
                            $('#div_cu').dialog('close');
                        }else{
                            $.messager.alert("消息提示", "失败！"+data.errorMsg);
                        }
                    },error: function(data,status,e){
                        ajaxLoadEnd();//lodingEnd
                        $.messager.alert("失败", "文件太大请选择10MB以下的文件或者文件中有不能解析的内容！");
                    }
                });
        	}
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