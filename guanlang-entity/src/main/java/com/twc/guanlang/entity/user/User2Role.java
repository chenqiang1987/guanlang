package com.twc.guanlang.entity.user;


import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户角色表
 */
@Data
@Entity
@Table(name = "t_role_user")
public class User2Role extends BaseEntity {


    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "user_id")
    private Long userId;


}
