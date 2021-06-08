package com.twc.guanlang.entity.user;


import com.twc.guanlang.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 角色
 */
@Data
@Entity
@Table(name = "t_role")
public class Role extends BaseEntity {


    @Column(name="name")
    private String name;

    //private List<SystemUser> userList;

    @Column(name="nick_name")
    private String nickName;
}
