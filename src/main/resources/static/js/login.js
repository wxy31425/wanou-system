/**
 * 登录
 */

$(function(){
     layui.use(['form' ,'layer'], function() {
         var form = layui.form;
         var layer = layui.layer;
         form.on("submit(login)",function () {
             login();
             return false;
         });
         // var path=window.location.href;
         // if(path.indexOf("kickout")>0){
         //     layer.alert("您的账号已在别处登录；若不是您本人操作，请立即修改密码！",function(){
         //         window.location.href="/login";
         //     });
         // }
     })
 })

function login(){
    $.ajax({
        url: "login",
        type: "POST",
        data: $("#useLogin").serialize(),
        dataType: "text",
        timeout: 20000,
        success: function (data) {
            alert(data)
            if (data == "true") {
                window.location.href = "toIndex.do";
            } else {
                layer.alert("账号密码错误",function(){
                    layer.closeAll();//关闭所有弹框
                });
            }
        }
    });
    setTimeout(function () {
        layer.alert("登录超时",function(){
            layer.closeAll();//关闭所有弹框
        });
    }, 20000);
}

