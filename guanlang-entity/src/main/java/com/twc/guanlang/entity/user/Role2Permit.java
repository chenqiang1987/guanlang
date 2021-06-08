package com.twc.guanlang.entity.user;


import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色资源表
 */
@Data
@Entity
@Table(name = "t_permit_role")
public class Role2Permit extends BaseEntity {



    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permit_id")
    private Integer permitId;


}
