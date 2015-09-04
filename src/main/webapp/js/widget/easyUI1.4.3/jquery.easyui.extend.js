//这个是公用方法
//采用jquery easyui loading css效果 
function ajaxLoading(){ 
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
 } 
function ajaxLoadEnd(){ 
     $(".datagrid-mask").remove(); 
     $(".datagrid-mask-msg").remove();             
}

/*
 * 
 * 示例
 $.ajax({ 
	type: 'POST', 
	url: 'sendLettersAgain.action', 
	data: {id:obj.id}, 
	beforeSend:ajaxLoading,发送请求前打开进度条 
	success: function(robj){ 
		ajaxLoadEnd();任务执行成功，关闭进度条 
	} 
}); */