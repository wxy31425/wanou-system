package com.wanou.system.service;


import com.wanou.system.model.user;
import com.wanou.system.repository.userRepository;
import com.wanou.system.util.HttpUtils;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户数据信息
 */
@Service
public class userService {
    @Autowired
    userRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(userService.class);


    /**
     * 意向客户上传的信息
     */
    public user addOneUser(String name, String job, String phone, String address, String remark,
                           String longitude, String latitude, String code) {
        user user = new user();
        Map<String, Object> map = new HashMap<String, Object>();
        String appId="wx66ecfdbfcc4c7cf6";
        String secret = "6af60051f44e04a8ef0e2c6a3b98e779";
        String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        try {
            if(StringUtils.isBlank(code)){
                logger.warn("code值无效");
            }else {
                String requestUrl = WX_URL.replace("APPID",appId).
                        replace("SECRET",secret ).replace("JSCODE", code).
                        replace("authorization_code", "authorization_code");
                logger.info(requestUrl);
                // 发起GET请求获取凭证
                JSONObject jsonObject = HttpUtils.httpsRequest(requestUrl, "GET", null);
                if (jsonObject != null) {
                    try {

                        user.setName(name);
                        user.setJob(job);
                        user.setPhone(phone);
                        user.setAddress(address);
                        user.setRemark(remark);
                        user.setUploadTime(new Date());
                        user.setLongitude(Double.parseDouble(longitude));
                        user.setLatitude(Double.parseDouble(latitude));
                        user.setWxId( map.put("openid", jsonObject.getString("openid")).toString());
                        user.setBgColor("#fff");
                        user.setBorderColor("#000");
                        user.setBorderWidth(2);
                        user.setColor("#ff0000");
                        user.setPadding(5);
                        user.setContent(job + "\n"+ name);
                        userRepository.save(user);
                    } catch (JSONException e) {
                        logger.warn("code值无效");
                    }
                }else {
                    logger.warn("code值无效");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);

        }
        return user;
    }

    /**
     * 根据意向客户的昵称查询
     */
    public List<user> findUserByWxId(String code){
        Map<String, Object> map = new HashMap<String, Object>();
        String appId="wx66ecfdbfcc4c7cf6";
        String secret = "6af60051f44e04a8ef0e2c6a3b98e779";
        String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        try {
            if(StringUtils.isBlank(code)){
                logger.warn("code值无效");
            }else {
                String requestUrl = WX_URL.replace("APPID",appId).
                        replace("SECRET",secret ).replace("JSCODE", code).
                        replace("authorization_code", "authorization_code");
                logger.info(requestUrl);
                // 发起GET请求获取凭证
                JSONObject jsonObject = HttpUtils.httpsRequest(requestUrl, "GET", null);
                if (jsonObject != null) {
                    try {
                        logger.info("要查询的实体wxId:"+   map.put("openid", jsonObject.getString("openid")));
                        return this.userRepository.findAllByWxId(map.put("openid", jsonObject.getString("openid")).toString());
                    } catch (JSONException e) {
                        logger.warn("code值无效");
                    }
                }else {
                    logger.warn("code值无效");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

}
