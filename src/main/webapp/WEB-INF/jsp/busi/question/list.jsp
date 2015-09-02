<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ include file="/include.taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/include.script.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列表</title>
<style type="text/css">
    body{margin:0}
</style>
<script type="text/javascript">
function fillGird(){
    $('#mainGrid').datagrid({
        url:baseUrl+"/question/query/jsonlist",
        queryParams: {},
        width:'98%',
        height:'100%',
        title:"",
        fit:true,
        pageSize:20,
        pageList:[10,20],
        striped:true,
        loadMsg:"数据加载中请稍后……",
        fitColumns:true,
        pagination:true,
        autoRowHeight:true,
        rownumbers:true,
        nowrap:false,
        columns:[[
            {field:'id',checkbox:true},
            {field:'content',title:'题目内容',width:250},
            {field:'answer',title:'答案',width:300},
            {field:'enName',title:'所属科目(English)',width:100},
            {field:'zhName',title:'所属科目(中文)',width:100},
            {field:'updateTime',title:'更新时间',width:150}
        ]],
        toolbar:[{
            text:'新增',
            iconCls:'icon-add',
            handler:function(){
                $('#div_cu').dialog({
                	title:"新增",
                	height:"50%",
                	width:"50%",
                	close:false,
                	cache:false,
                	href:baseUrl+"/question/query/addUI",
                	modal:true
                });
            }
        },{
            text:'修改',
            iconCls:'icon-edit',
            handler:function(){
                var rows = $("#mainGrid").datagrid("getSelections");
                if(rows.length<1){
                    $.messager.alert("消息提示", "请选择一条记录！","warning");
                    return false;
                }else if(rows.length>1){
                    $.messager.alert("消息提示", "只能选择一条记录！","warning");
                    return false;
                }
                var id= rows[0].id;
                if(id){
                	$('#div_cu').dialog({
                        title:"修改",
                        height:"50%",
                        width:"50%",
                        close:false,
                        cache:false,
                        href:baseUrl+"/question/query/updateUI?id="+id,
                        modal:true
                    });
                }
            }
        },{
            text:'删除',
            iconCls:'icon-remove',
            handler:function(){
                var rows = $("#mainGrid").datagrid("getSelections");
                if(rows.length<1){
                    $.messager.alert("消息提示", "请选择一条记录！","warning");
                    return false;
                }
                var affirm=$.messager.confirm("消息提示", "您确认删除吗？此操作不可回退！",function(r){
                    if(r){
                        var ids="";
                        for (var int = 0; int < rows.length; int++) {
                            ids+=rows[int].id+",";
                        }
                        //casual find a if sentence to limit ajax request
                        if(ids.length>0){
                            $.ajax({
                                type : "POST",
                                url : baseUrl+"/question/delete",
                                data:{ids:ids},
                                success : function(data) {
                                    if(data.state==200){
                                        $.messager.alert("消息提示", "成功！");
                                        $('#mainGrid').datagrid("reload");
                                    }else{
                                        $.messager.alert("消息提示", "抱歉，数据异常请重试！","warning");
                                    }
                                }
                            })
                        }
                    }
                });
            }
        }]
    });
}

$(function(){
    fillGird();
});
</script>
</head>
<body>
    <div class="easyui-datagrid" id="mainGrid"></div>
    <div id="div_cu"></div>
</body>
</html>