package com.wanou.system.repository;

import com.wanou.system.model.adminstrator;
import com.wanou.system.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface userRepository extends JpaRepository<user, String>, JpaSpecificationExecutor<user> {
    /**
     * 根据微信wxId
     * @param
     * @return
     */
    List<user> findByWxIdAndPerm(String wxId,Integer perm);

    /**
     * 根据id授权
     * @param id
     */
    @Transactional
    @Modifying
    @Query("update user set perm = 1 where id in(?1)")
    void updateUserPerm(int id);

    /**
     * 根据id删除
     * @param id
     */
    @Transactional
    @Modifying
    @Query("update user set isDelete = 1 where id in(?1)")
    void delUser(List<Integer> id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    user findById(int id);

}
