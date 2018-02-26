/**
 * 
 */

$(function() {
	$("#add-review").dialog('close');

});


function newReview(){
    clearReviewForm();
	$("#add-review").dialog('open');
}

function doReSearch(){
    $('#review-dg').datagrid('load',{
        taskName: $('#queryReTaskName').val(),
        taskStartTimeStr: $('#queryReStartTime').val(),
        taskEndTimeStr: $('#queryReEndTime').val()
    });
}


function closeReview(){
    $("#add-review").dialog('close');
    clearReviewForm();
}

function clearReviewForm(){

    $('#reviewForm input').val('');

}

function deleteReview(){
	
	var selectId = $(".datagrid-row-selected [field='id'] div").html();
	
	if(selectId == 'undefined'){
		alert("请选择要删除的活动！");
	}
	
	if(confirm("是否要删除活动" + selectId + "?")){
		
		$.ajax({
			
			url:"/reviews/"+selectId,
			type:"delete",
			success:function(data){
				if(data.code == 0){
                    doReSearch();
				} else
				{
					alert(data.comment)
				}
			}
			
		});
		
	}
}

function saveReview(){

    var taskName = $("#addReTaskName").val();
    var viewLevel = $("#addReViewLevel").val();
    var taskStartTime = $("#addReTaskStartTime").val();
    var taskEndTime =  $("#addReTaskEndTime").val();
    var taskDetailResourceUrl = $("#returnReTaskDetailResourceUrl").val();

    if(taskName == ''
		|| viewLevel == ''
        || taskStartTime == ''
		|| taskEndTime == ''
		|| taskDetailResourceUrl == '' ){

        alert("活动信息不完整，请填写所有字段！！");
        return;
    }

    $.ajax({
        url:"/reviews",
        type:"post",
        data:{
            taskName : taskName,
            viewLevel : viewLevel,
            taskStartTime : taskStartTime,
            taskEndTime : taskEndTime,
            taskDetailResourceUrl : taskDetailResourceUrl
        },
        dataType:"json",
        success:function(data){
            alert(data.data);
            if(data.code == 0){
                closeReview();
                doReSearch();
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
