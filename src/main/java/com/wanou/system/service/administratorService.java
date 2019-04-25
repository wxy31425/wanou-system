package com.wanou.system.service;


import com.wanou.system.model.adminstrator;
import com.wanou.system.repository.administratorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 管理员登录
 */
@Service
public class administratorService implements UserDetailsService {
    @Autowired
    administratorRepository administratorRepository;

    private final Logger logger = LoggerFactory.getLogger(administratorService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        adminstrator userDetail = administratorRepository.findByLoginNameLowerCase(username.toLowerCase());
        if (userDetail == null) {
            throw new UsernameNotFoundException("account:" +username+ " not found");
        }
        userDetail.setLoginTime(new Date());
        try {
            userDetail.setLoginIP(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        administratorRepository.save(userDetail);
        return userDetail;
    }

}
