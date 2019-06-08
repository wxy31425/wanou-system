package com.wanou.system.service;


import com.wanou.system.controller.vm.userVm;
import com.wanou.system.model.user;
import com.wanou.system.repository.userRepository;
import com.wanou.system.util.HttpUtils;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户数据信息
 */
@Service
public class userService {
    @Autowired
    userRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(userService.class);

    /**
     * 分页查询，包含查询条件(移动端使用)
     *
     * @param
     * @return
     */
    public Page<user> getUserPaged(String startTime,String endTime,Integer perm,Integer page,Integer limit) {
        /*1.从map中取出查询条件，包括页号和每页大小*/
        /*2.构建PageRequest实例用于分页*/
        logger.info("当前页号为：" + page);
        logger.info("每页大小为：" + limit);
        PageRequest pageRequest = new PageRequest(page-1, limit, new Sort(Sort.Direction.DESC, "uploadTime"));
        /*3.传入specification(lambad实现)和pageRequest，发起查询并返回数据*/
        Page<user> pages = this.userRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();//保存所有查询条件
            Path<Integer> isDelatePath = root.get("isDelete");
            predicates.add(criteriaBuilder.equal(isDelatePath, 0));
             if(perm!=null){
                 if (perm == 0) {
                     Predicate predicatedeviceType = criteriaBuilder.equal(root.get("perm"), 0);
                     predicates.add(predicatedeviceType);
                 } else if (perm == 1){
                     Predicate predicatedeviceType = criteriaBuilder.equal(root.get("perm"), 1);
                     predicates.add(predicatedeviceType);
                 }
             }

            if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
                Date beginDate = null;
                Date endDate = null;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    beginDate = formatter.parse(startTime);
                    endDate = formatter.parse(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Predicate predicateCreateDateBetween = criteriaBuilder.between(root.get("uploadTime"), beginDate, endDate);
                predicates.add(predicateCreateDateBetween);
            }
            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
                    logger.info("最终构建的查询条件为：" + finalPredicate);
                    return finalPredicate;
        }, pageRequest);
        logger.info("返回的页面数据为" + pages);
        return pages;
    }
    /**
     * 意向客户上传的信息
     */
    public user addOneUser(String name, String job, String phone, String address, String remark,
                           String longitude, String latitude, String code,String yhxx) {
        user user = new user();
        Map<String, Object> map = new HashMap<String, Object>();
        String appId="wxb154d97061942505";
        String secret = "59f36b43550d55a74ec1395ec06289c2";
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
                logger.info("微信绑定OpenId："+ jsonObject.toString());
                if (jsonObject != null) {
                    map.put("openid", jsonObject.getString("openid"));
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
                        JSONObject jsonObj = JSONObject.fromObject(yhxx);
                        String nickName = jsonObj.getString("nickName");
                        user.setWxName(nickName);
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
        String appId="wxb154d97061942505";
        String secret = "59f36b43550d55a74ec1395ec06289c2";
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
                        return this.userRepository.findByWxIdAndPerm(map.put("openid", jsonObject.getString("openid")).toString(),1);
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

    /**
     * 授权
     * @param id
     * @return
     */
    public boolean findUserPerm(int id){
        this.userRepository.updateUserPerm(id);
        return true;
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    public boolean delUser(int[] ids){
        Integer[] is = ArrayUtils.toObject(ids);
        List<Integer> idArr = Arrays.asList(is);
        this.userRepository.delUser(idArr);
        return true;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
     public user findByUser(int id){
         return this.userRepository.findById(id);
     }
    /**
     * 编辑更新
     * @param userVm
     * @return
     */
    public boolean modifyOneUser(userVm userVm){
         user oldUser = userRepository.findById(userVm.getId());
         oldUser.setName(userVm.getName());
         oldUser.setJob(userVm.getJob());
         oldUser.setPhone(userVm.getPhone());
         oldUser.setAddress(userVm.getAddress());
         oldUser.setRemark(userVm.getRemark());
          this.userRepository.save(oldUser);
          return true;
    }

    /**
     * 根据地址查询Marker
     * @param address
     * @return
     */
    public List<user> getUserAddress(String address){
        List<user> markerList = this.userRepository.findAll((root, criteriaQuery, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<Predicate>();
            if (!StringUtils.isEmpty(address)) {
                String addressMarker = "%" + address + "%";
                Predicate userAddressMarkerPredicate = criteriaBuilder.like(root.get("address"), addressMarker);
                predicates.add(userAddressMarkerPredicate);
            }
            Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            return finalPredicate;
        });
        return markerList;
    }


}
