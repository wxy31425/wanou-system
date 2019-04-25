package com.wanou.system.repository;

import com.wanou.system.model.adminstrator;
import com.wanou.system.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface userRepository extends JpaRepository<user, String>, JpaSpecificationExecutor<user> {
    /**
     * 根据微信头像查询
     * @param nickName
     * @return
     */
    user findAllByNickName(String nickName);

}
