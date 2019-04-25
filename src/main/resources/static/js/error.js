/**
 * ajax异常
 * */
function ajaxError(){
	$.ajax({
		url: "/error/ajaxError",
		data:"",
		type: "GET",
		success:function(data){
			//异常过滤处理
			if(isError(data)){
				alert(data);
			}
		},
		error:function(e){
			//也可通过error控制请求失败的情况
			console.log("e:"+e);
		}
	});
}











