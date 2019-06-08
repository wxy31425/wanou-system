package com.wanou.system.controller;

import com.wanou.system.controller.vm.userVm;
import com.wanou.system.model.user;
import com.wanou.system.service.userService;
import com.wanou.system.tools.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class userController {
    @Autowired
    userService userService;
    /**
     * 客户跳转页面
     * @return
     */
    @RequestMapping("toUser")
    public String toUser() {
        return "user/user_list";
    }

    /**
     * 分页查询多个实体
     * @return
     */
    @ResponseBody
    @GetMapping(value = "userQueryPage")
    public RestResponse queryPage(String startTime,String endTime,Integer perm,
                                  @RequestParam(required = false, defaultValue = "0") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer limit) {
        Page <user> userPage = userService.getUserPaged(startTime,endTime,perm,page,limit);
        return RestResponse.msgReturn(userPage.getTotalElements(),userPage.getContent());
    }

    /**
     * 客户授权
     * @return
     */
    @PostMapping("userPermBatch")
    @ResponseBody
    public RestResponse userPermBatch(int id) {
        boolean perm = userService.findUserPerm(id);
        if (perm = true) {
            return RestResponse.success(perm, "授权成功");
        } else {
            return RestResponse.failure(perm, "授权失败");
        }
    }

    /**
     * 客户删除
     * @return
     */
    @PostMapping("userDelBatch")
    @ResponseBody
    public RestResponse userDelBatch( int [] ids) {
        boolean perm = userService.delUser(ids);
        if (perm = true) {
            return RestResponse.success(perm, "删除成功");
        } else {
            return RestResponse.failure(perm, "删除失败");
        }
    }

    /**
     * 跳转编辑
     * @return
     */
    @RequestMapping("toEdit")
    public String toEdit(Model model,Integer id) {
        user userModel = userService.findByUser(id);
        model.addAttribute("user",userModel);
        return "user/user_edit";
    }

    /**
     * 客户编辑
     * @return
     */
    @PostMapping("modifyOneUser")
    @ResponseBody
    public RestResponse modifyOneUser( userVm userVm) {
         boolean oldUser = userService.modifyOneUser(userVm);
        if (oldUser = true) {
            return RestResponse.success(oldUser, "更新成功");
        } else {
            return RestResponse.failure(oldUser, "更新失败");
        }
    }

    /**
     * 查询所有Marker
     * @return
     */
    @GetMapping("queryMarker")
    @ResponseBody
    public List<user> queryMarker(String address){
       return userService.getUserAddress(address);
    }





}
