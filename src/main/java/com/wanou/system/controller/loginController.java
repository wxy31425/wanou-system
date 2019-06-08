package com.wanou.system.controller;

import com.wanou.system.model.adminstrator;
import com.wanou.system.model.user;
import com.wanou.system.service.administratorService;
import com.wanou.system.tools.Encrypt;
import com.wanou.system.tools.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class loginController {
    @Autowired
    administratorService adminstratorService;
    Logger log = LoggerFactory.getLogger(loginController.class);
    /**
     * 跳转首页
     * @return
     */
    @RequestMapping("toIndex")
    public String toIndex(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        model.addAttribute("userDetails",userDetails);
        return "index";
    }

    /**
     * 退出注销
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("logout")
    public String loginOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "login";
    }

    /**
     * 跳转修改密码页面
     * @return
     */
    @RequestMapping("updatePwd")
    public String updatePwd() {
        return "upassword";
    }


    @PostMapping("savePwd")
    @ResponseBody
    public RestResponse savePwd(@RequestParam String password, @RequestParam String newPassword, Principal principal) throws Exception {
        Authentication authenticationToken = (Authentication) principal;
        adminstrator admin = (adminstrator) authenticationToken.getPrincipal();
        if (!Encrypt.encryptRMS(password).equals(admin.getPassword())) {
            return RestResponse.failure(false, "原密码错误");
        }
        admin.setPassword(Encrypt.encryptRMS(newPassword));
        adminstratorService.save(admin);
        return RestResponse.success(true, "密码更新成功");
    }

}
