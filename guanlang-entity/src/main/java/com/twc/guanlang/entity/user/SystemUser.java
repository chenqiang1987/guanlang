package com.twc.guanlang.entity.user;


import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户
 */
@Data
@Entity
@Table(name = "t_system_user")
public class SystemUser extends BaseEntity {

    public static final String DEFALUST_PASSWORD = "123456";

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "tel_phone")
    private String telPhone;

    @Column(name = "salt")
    private String salt;

    @Column(name = "nick_name")
    private String nickName;

    public String getCredentialsSalt() {
        return userName + salt + salt;
    }

}
