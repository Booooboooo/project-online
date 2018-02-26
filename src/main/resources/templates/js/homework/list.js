/**
 * 
 */

$(function() {
	$("#add-homework").dialog('close');

});


function newHomework(){
    clearHomeworkForm();
	$("#add-homework").dialog('open');
}

function doHomeworkSearch(){
    $('#homework-dg').datagrid('load',{
        taskName: $('#queryHomeworkTaskName').val(),
        taskStartTimeStr: $('#queryHomeworkStartTime').val(),
        taskEndTimeStr: $('#queryHomeworkEndTime').val()
    });
}


function closeHomework(){
    $("#add-homework").dialog('close');
    clearHomeworkForm();
}

function clearHomeworkForm(){

    $('#homeworkForm input').val('');

}

function deleteHomework(){
	
	var selectId = $(".datagrid-row-selected [field='id'] div").html();
	
	if(selectId == 'undefined'){
		alert("请选择要删除的作业！");
	}
	
	if(confirm("是否要删除作业" + selectId + "?")){
		
		$.ajax({
			
			url:"/homework/"+selectId,
			type:"delete",
			success:function(data){
				if(data.code == 0){
                    doHomeworkSearch();
				} else
				{
					alert(data.comment)
				}
			}
			
		});
		
	}
}

function saveHomework(){

    var taskName = $("#addHomeworkTaskName").val();
    var viewLevel = $("#addHomeworkViewLevel").val();
    var taskStartTime = $("#addHomeworkTaskStartTime").val();
    var taskEndTime =  $("#addHomeworkTaskEndTime").val();
    var taskDetailResourceUrl = $("#returnHomeworkTaskDetailResourceUrl").val();

    if(taskName == ''
		|| viewLevel == ''
        || taskStartTime == ''
		|| taskEndTime == ''
		|| taskDetailResourceUrl == '' ){

        alert("作业信息不完整，请填写所有字段！！");
        return;
    }

    $.ajax({
        url:"/homework",
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
                closeHomework();
                doHomeworkSearch();
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
