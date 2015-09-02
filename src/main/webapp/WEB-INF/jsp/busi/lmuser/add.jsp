<%@ page pageEncoding="UTF-8"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/additional-methods.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/additional-methods-lm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/validate/messages_zh.js"></script>
<script type="text/javascript">
<%-- Ztree --%>
var treeObj=null;
var treeNode=eval('(${list})');
var setting = {
	check:{
		enable:true,
		radioType:"level"
	},
    data: {
    	key:{
    		name:"zhName"
    	},
        simpleData: {
            enable: true,
            idKey: "id"
            
        }
    }, callback: {
    	onCheck: function(event,treeId,treeNode){
    		var nodes = treeObj.getCheckedNodes(true);
    		var sIds="";
    		var sZhNames="";
    		for (var i = 0; i < nodes.length; i++) {
    			sIds+=nodes[i].id+",";
                sZhNames+=nodes[i].zhName+",";
			}
    		$("#sIds").val(sIds.substring(0,sIds.length-1));
    		$("#sZhNames").val(sZhNames.substring(0,sZhNames.length-1));
        }
    }
};

$(function(){
    $("#form").validate({
        rules:{
        	userName:{
                required:true,
                mobilePhone:true,
                remote:{
                    url:"${pageContext.request.contextPath}/lmuser/query/check",
                    type:"post"
                }
            },
            userMac:{
            	maxlength:20,
                remote:{
                    url:"${pageContext.request.contextPath}/lmuser/query/check",
                    type:"post"
                }
            },
            userProofRule:{
            	maxlength:80,
                remote:{
                    url:"${pageContext.request.contextPath}/lmuser/query/check",
                    type:"post"
                }
            }
        }, messages: {
        	userName:{
                remote:"此用户名已存在,请重新输入!"
            },
            userMac:{
                remote:"此用户MAC地址已存在,请重新输入!"
            },
            userProofRule:{
                remote:"此用户令牌已存在,请重新输入!"
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
    
    //选择角色
    $("#pick").click(function(){
    	treeObj=$.fn.zTree.init($("#subjectTree"), setting, treeNode);
    });
    
});
</script>
<!-- 新增页面 -->
<form id="form" method="post" action="${pageContext.request.contextPath}/lmuser/add">
    <table>
        <tr>
            <td>用户名:</td>
            <td>
                <input type="text" name="userName" id="userName" />
            </td>
        </tr>
        <tr>
            <td>用户MAC地址:</td>
            <td>
                <textarea name="userMac" id="userMac"></textarea>
            </td>
        </tr>
        <tr>
            <td>令牌:</td>
            <td>
                <textarea name="userProofRule" id="userProofRule"></textarea>
            </td>
        </tr>
        <tr>
            <td>选择科目：</td>
            <td>
                <textarea id="sZhNames" name="sZhNames" readonly="readonly"></textarea>
                <input type="hidden" name="sIds" id="sIds" />
                <input type="button" id="pick" value="选择" />
                <ul id="subjectTree" class="ztree"></ul>
            </td>
        </tr>
        <tr>
            <td>是否可用:</td>
            <td>
                <input type="radio" name="isActive" id="isActive0" value="0" checked="checked" />否
                <input type="radio" name="isActive" id="isActive1" value="1" />是
            </td>
        </tr>
    </table>
    <div style="text-align:center;padding:5px">
        <input type="submit" value="确定"/>
        <input type="reset" value="重置"/>
	</div>
</form>