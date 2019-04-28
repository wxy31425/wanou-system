package com.wanou.system.api;


import com.wanou.system.model.user;
import com.wanou.system.service.userService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/")
public class userApi {
    @Autowired
     userService userService;

    @ApiOperation(value = "意向客户上传的信息")
    @CrossOrigin
    @GetMapping("add")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="客户姓名",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="zy",value="客户职业",dataType="String", paramType = "query") ,
            @ApiImplicitParam(name="dh",value="客户电话",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="dz",value="客户地址",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="bz",value="备注",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="lat",value="经度",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="lon",value="纬度",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="code",value="微信code",dataType="String", paramType = "query"),
    })
    public ResponseEntity<user> add (String name, String zy, String dh,
                                     String dz, String bz, String lon,
                                     String lat,String code){
        user user =  userService.addOneUser(name,zy,dh,dz,bz,lon,lat, code);
        return ResponseEntity.ok(user);
     }

//    @ApiOperation(value = "根据意向客户的微信code查询气泡标注点")
//    @GetMapping("findUserByWxId")
//    @ApiImplicitParam(name="wxId",value="微信code",dataType="String", paramType = "query")
//    @ResponseBody
//    public List<user> findUserByMarker(String code){
//        return userService.findUserByWxId(code);
//    }



    @ApiOperation(value = "根据意向客户的微信code查询")
    @CrossOrigin
    @GetMapping("findUserByWxId")
    @ApiImplicitParam(name="code",value="code",dataType="String", paramType = "query")
    @ResponseBody
    public List<user> findUserByWxId (String code){
        return userService.findUserByWxId(code);
    }


}
