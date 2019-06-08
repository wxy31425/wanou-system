package com.wanou.system.model;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
public class user  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 客户职业
     */
    private String job;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 备注信息
     */
    @Column(length = 1000)
    private String remark;

    /**
     * 坐标经度
     */
    private double longitude;

    /**
     * 坐标纬度
     */
    private double latitude;

    /**
     * 上传时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    /**
     * 微信用户wxId
     */
    private String wxId;

    /**
     * 内容
     */
    private String content;

    /**
     * 权限
     * 1：授权 2：未授权
     */
     private int perm = 0;

    /**
     * 状态删除
     */
    private int isDelete = 0;

    /**
     * 微信用户昵称
     */
    private String wxName;

    private String flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPerm() {
        return perm;
    }

    public void setPerm(int perm) {
        this.perm = perm;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
