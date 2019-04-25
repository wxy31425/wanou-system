package com.wanou.system.repository;

import com.wanou.system.model.adminstrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface administratorRepository extends JpaRepository<adminstrator, String>, JpaSpecificationExecutor<adminstrator> {

    /**
     * 判断账号是否正确登录
     * @param account
     * @return
     */
    @Query("from adminstrator where lower(account)=?1")
    adminstrator findByLoginNameLowerCase(String account);
}
