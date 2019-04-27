package com.wanou.system.repository;

import com.wanou.system.model.adminstrator;
import com.wanou.system.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface userRepository extends JpaRepository<user, String>, JpaSpecificationExecutor<user> {
    /**
     * 根据微信wxId
     * @param
     * @return
     */
    List<user> findAllByWxId(String wxId);

}
