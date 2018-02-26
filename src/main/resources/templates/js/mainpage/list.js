/**
 * 
 */

$(function() {
	$("#add-page").dialog('close');
	
});


function newPage(){
	clearForm();
	$("#add-page").dialog('open'); 
}


function closePage(){
	$("#add-page").dialog('close'); 
	clearForm();
}

function clearForm(){
	
	$('#pageForm input').val('');
	
}

function savePage(){
	
	var fakeName = $("#addFakeName").val();
	var pageUrl = $("#returnPageUrl").val();
	
	if(pageUrl == '' ){
		alert("图片信息不完整");
		return;
	}
	
	$.ajax({
		url:"/global/mainpage/insert.json",
		type:"post",
		data:{
			fakeName : fakeName,
			pageUrl : pageUrl
		},
		dataType:"json",
		success:function(data){
			alert(data.data);
			if(data.code == 0){
				closePage();
			}
		}
		
	});
	
}

function uploadResource(uploadId, urlBoxId){
	
	$("#"+urlBoxId).val("");
	
	$.messager.progress({ 
	       title: '提示', 
	       msg: '文件上传中，请稍候……', 
	       text: '' 
	});
	
	$.ajaxFileUpload({
	    type:'POST',
	    url : '/file/upload?fileCat='+uploadId,
	    secureuri : false,
	    fileElementId :uploadId,
	    dataType : 'json',
	    success : function(data) { //上传成功后的回调。
	        if(data.code ==0){
	        	alert("文件上传成功！");
	        	$("#"+urlBoxId).val(data.data);
	        }else {
	            alert(data.comment);
	        }
	        $.messager.progress('close');
	    }
	});

}
