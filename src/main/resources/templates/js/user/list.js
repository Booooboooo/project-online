/**
 * 
 */

$(function() {
	$("#add-user").dialog('close');

});


function newUser(){
    clearUserForm();
	$("#add-user").dialog('open');
}

function doUserSearch(){
    $('#user-dg').datagrid('load',{
        name: $('#queryName').val(),
        eduLevel: $('#queryEduLevel').val(),
        sex: $('#querySex').val()
    });
}


function closeUser(){
    $("#add-user").dialog('close');
    clearUserForm();
}

function clearUserForm(){

    $('#userForm input').val('');

}

function deleteUser(){
	
	var selectId = $(".datagrid-row-selected [field='id'] div").html();
	
	if(selectId == 'undefined'){
		alert("请选择要删除的用户！");
	}
	
	if(confirm("是否要删除用户" + selectId + "?")){
		
		$.ajax({
			
			url:"/users/"+selectId,
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

function saveUser(){

    var addUserName = $("#addUserName").val();
    var addTel = $("#addTel").val();
    var addEduLevel = $("#addEduLevel").val();
    var addSex =  $("#addSex").val();
    var addBirthday = $("#addBirthday").val();

    if(addUserName == ''
		|| addTel == ''
        || addEduLevel == ''
		|| addSex == ''
		|| addBirthday == '' ){

        alert("用户信息不完整，请填写所有字段！！");
        return;
    }

    $.ajax({
        url:"/users",
        type:"post",
        data:{
            name : addUserName,
            tel : addTel,
            eduLevel : addEduLevel,
            sex : addSex,
            birthday : addBirthday
        },
        dataType:"json",
        success:function(data){
            alert(data.data);
            if(data.code == 0){
                closeUser();
                doUserSearch();
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
