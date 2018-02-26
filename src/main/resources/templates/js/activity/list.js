/**
 * 
 */

$(function() {
	$("#add-active").dialog('close');
	
});


function newActivity(){
	clearActiveForm();
	$("#add-active").dialog('open'); 
}


function closeActive(){
	$("#add-active").dialog('close'); 
	clearActiveForm();
}

function clearActiveForm(){
	
	$('#activeForm input').val('');
	
}

function saveActive(){
	
	var taskName = $("#addTaskName").val();
	var taskTitle = $("#addTaskTitle").val();
	var viewLevel = $("#addViewLevel").val();
	var taskDesc = $("#addTaskDesc").val();
	var viewLeader = $("#addViewLeader").val();
	var viewArea = $("#addViewArea").val();
	var taskStartTime = $("#addTaskStartTime").val();
	var taskEndTime =  $("#addTaskEndTime").val();
	var taskBriefResourceUrl = $("#returnTaskBriefResourceUrl").val();
	var taskDetailResourceUrl = $("#returnTaskDetailResourceUrl").val();
	
	if(taskName == '' || taskTitle == '' 
		|| viewLevel == '' || taskDesc == '' 
			|| viewLeader == '' || viewArea == '' 
				|| taskStartTime == '' || taskEndTime == '' 
					|| taskBriefResourceUrl == '' || taskDetailResourceUrl == '' ){
		
		alert("活动信息不完整，请填写所有字段！！");
		return;
	}
	
	$.ajax({
		url:"/activity/insert.json",
		type:"post",
		data:{
			taskName : taskName,
			taskTitle : taskTitle,
			viewLevel : viewLevel,
			taskDesc : taskDesc,
			viewLeader : viewLeader,
			viewArea : viewArea,
			taskStartTime : taskStartTime,
			taskEndTime : taskEndTime,
			taskBriefResourceUrl : taskBriefResourceUrl,
			taskDetailResourceUrl : taskDetailResourceUrl
		},
		dataType:"json",
		success:function(data){
			alert(data.data);
			if(data.code == 0){
				closeActive();
				doSearch();
			}
		}
		
	});
	
}


function doSearch(){
	$('#active-dg').datagrid('load',{
		activeName: $('#queryTaskName').val(),
		startTime: $('#queryStartTime').val(),
		endTime: $('#queryEndTime').val()
	});
}


function deleteActivity(){
	
	var selectId = $(".datagrid-row-selected [field='id'] div").html();
	
	if(selectId == 'undefined'){
		alert("请选择要删除的活动！");
		return;
	}
	
	if(confirm("是否要删除活动" + selectId + "?")){
		
		$.ajax({
			
			url:"/activity/delete.json?id="+selectId,
			type:"get",
			success:function(data){
				alert(data.data);
				if(data.code == 0){
					doSearch();
				}
			}
			
		});
		
	}
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
