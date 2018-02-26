/**
 * 
 */

$(function() {
	$("#add-preparation").dialog('close');

});


function newPreparation(){
    clearPreparationForm();
	$("#add-preparation").dialog('open');
}

function doPreSearch(){
    $('#preparation-dg').datagrid('load',{
        taskName: $('#queryPreTaskName').val(),
        taskStartTimeStr: $('#queryPreStartTime').val(),
        taskEndTimeStr: $('#queryPreEndTime').val()
    });
}


function closePreparation(){
    $("#add-preparation").dialog('close');
    clearPreparationForm();
}

function clearPreparationForm(){

    $('#preparationForm input').val('');

}

function deletePreparation(){
	
	var selectId = $(".datagrid-row-selected [field='id'] div").html();
	
	if(selectId == 'undefined'){
		alert("请选择要删除的活动！");
	}
	
	if(confirm("是否要删除活动" + selectId + "?")){
		
		$.ajax({
			
			url:"/preparations/"+selectId,
			type:"delete",
			success:function(data){
				if(data.code == 0){
                    doPreSearch();
				} else
				{
					alert(data.comment)
				}
			}
			
		});
		
	}
}

function savePreparation(){

    var taskName = $("#addPreTaskName").val();
    var viewLevel = $("#addPreViewLevel").val();
    var taskStartTime = $("#addPreTaskStartTime").val();
    var taskEndTime =  $("#addPreTaskEndTime").val();
    var taskDetailResourceUrl = $("#returnPreTaskDetailResourceUrl").val();

    if(taskName == ''
		|| viewLevel == ''
        || taskStartTime == ''
		|| taskEndTime == ''
		|| taskDetailResourceUrl == '' ){

        alert("活动信息不完整，请填写所有字段！！");
        return;
    }

    $.ajax({
        url:"/preparations",
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
                closePreparation();
                doPreSearch();
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
