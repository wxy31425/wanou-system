package com.wanou.system.service;


import com.wanou.system.model.user;
import com.wanou.system.repository.userRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
                           String longitude, String latitude, String nickName) {
        user user = new user();
        user.setName(name);
        user.setJob(job);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRemark(remark);
        user.setUploadTime(new Date());
        user.setLongitude(Double.parseDouble(longitude));
        user.setLatitude(Double.parseDouble(latitude));
        user.setNickName(nickName);
         userRepository.save(user);
        return user;
    }

    /**
     * 根据意向客户的昵称查询
     */
    public user findUserByNickname(String nickName){
        logger.info("要查询的实体id:"+nickName);
        return this.userRepository.findAllByNickName(nickName);
    }

}
