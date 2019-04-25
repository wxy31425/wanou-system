package com.wanou.system.api;


import com.wanou.system.model.user;
import com.wanou.system.service.userService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/")
public class userApi {
    @Autowired
     userService userService;

    @ApiOperation(value = "意向客户上传的信息")
    @PostMapping("add")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="客户姓名",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="zy",value="客户职业",dataType="String", paramType = "query") ,
            @ApiImplicitParam(name="dh",value="客户电话",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="dz",value="客户地址",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="bz",value="备注",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="lat",value="经度",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="lon",value="纬度",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="src",value="微信头像",dataType="String", paramType = "query")
    })
    public ResponseEntity<user> add (String name, String zy, String dh,
                                     String dz, String bz, String lon,
                                     String lat, String src){
        user user =  userService.addOneUser(name,zy,dh,dz,bz,lon,lat,src);
        return ResponseEntity.ok(user);
     }


    @ApiOperation(value = "根据意向客户的微信昵称查询")
    @GetMapping("findUserByNickname")
    @ApiImplicitParam(name="src",value="微信头像",dataType="String", paramType = "query")
    @ResponseBody
    public user findUserByNickname (String src){
        return userService.findUserByNickname(src);
    }


}
